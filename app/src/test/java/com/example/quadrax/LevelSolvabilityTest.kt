package com.example.quadrax

import com.example.quadrax.audio.SoundEffects
import com.example.quadrax.data.manager.LevelRepository
import com.example.quadrax.data.manager.ProgressStore
import com.example.quadrax.data.model.GridPosition
import com.example.quadrax.engine.QuadraxEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.ArrayDeque
import java.util.PriorityQueue
import kotlin.math.abs

class LevelSolvabilityTest {

    private class FakeProgressStore : ProgressStore {
        private var highest = 1
        private val best = mutableMapOf<Int, Int>()
        private var muted = true
        private var crt = true
        override fun getHighestUnlockedLevel(): Int = highest
        override fun unlockLevel(levelId: Int) { if (levelId > highest) highest = levelId }
        override fun getBestSteps(levelId: Int): Int = best[levelId] ?: 9999
        override fun saveBestSteps(levelId: Int, steps: Int) {
            if (steps < getBestSteps(levelId)) best[levelId] = steps
        }
        override fun isSoundMuted(): Boolean = muted
        override fun setSoundMuted(muted: Boolean) { this.muted = muted }
        override fun isCrtEnabled(): Boolean = crt
        override fun setCrtEnabled(enabled: Boolean) { this.crt = enabled }
    }

    private class NoOpSoundEffects : SoundEffects {
        override fun playStep() {}
        override fun playClimb() {}
        override fun playPush() {}
        override fun playThud() {}
        override fun playSwitchChar() {}
        override fun playLever() {}
        override fun playTeleport() {}
        override fun playCrumble() {}
        override fun playElevator() {}
        override fun playDeath() {}
        override fun playVictory() {}
        override fun stopVictorySong() {}
    }

    private enum class ActionType { LEFT, RIGHT, UP, DOWN, LEVER }
    private data class MoveAction(val explorerIdx: Int, val type: ActionType)

    private fun applyAction(engine: QuadraxEngine, action: MoveAction) {
        engine.selectExplorer(action.explorerIdx)
        when (action.type) {
            ActionType.LEFT -> engine.handleMove(-1, 0)
            ActionType.RIGHT -> engine.handleMove(1, 0)
            ActionType.UP -> engine.handleMove(0, -1)
            ActionType.DOWN -> engine.handleMove(0, 1)
            ActionType.LEVER -> engine.triggerAction()
        }
    }

    private data class WorldSig(
        val boulders: List<Long>,
        val leverMask: Int,
        val elevatorYs: List<Int>,
        val brokenTiles: Set<GridPosition>,
        val activeChannels: Set<String>
    )

    private fun worldSig(engine: QuadraxEngine, state: QuadraxEngine.CompactState): WorldSig {
        return WorldSig(
            boulders = state.boulders,
            leverMask = state.leverMask,
            elevatorYs = state.elevatorYs,
            brokenTiles = state.brokenTiles,
            activeChannels = engine.getActiveChannels()
        )
    }

    private data class SolveResult(
        val moves: List<MoveAction>,
        val macroStatesExplored: Int
    )

    private data class QueueNode(
        val state: QuadraxEngine.CompactState,
        val gSteps: Int,
        val fScore: Int
    ) : Comparable<QueueNode> {
        override fun compareTo(other: QueueNode): Int = fScore.compareTo(other.fScore)
    }

    private fun heuristic(
        engine: QuadraxEngine,
        state: QuadraxEngine.CompactState,
        spikePositions: List<GridPosition>
    ): Int {
        val def = engine.levelDef
        var h = 0

        // Distance of both explorers to exit
        h += (abs(state.rX - def.exitPos.x) + abs(state.rY - def.exitPos.y))
        h += (abs(state.bX - def.exitPos.x) + abs(state.bY - def.exitPos.y))

        // Penalize unpressed plates & guide boulders toward them
        for ((_, p) in engine.plates) {
            if (!p.active) {
                h += 28
                var minD = 30
                for (b in engine.boulders) {
                    val d = abs(b.x - p.x) + abs(b.y - p.y)
                    if (d < minD) minD = d
                }
                h += minD
            }
        }

        // Penalize uncovered spikes & guide boulders toward them
        for (sp in spikePositions) {
            if (engine.getBoulderAt(sp.x, sp.y) == null) {
                h += 28
                var minD = 30
                for (b in engine.boulders) {
                    val d = abs(b.x - sp.x) + abs(b.y - sp.y)
                    if (d < minD) minD = d
                }
                h += minD
            }
        }

        // Penalize closed gates
        for ((_, g) in engine.gates) {
            if (!g.open) h += 16
        }

        return h
    }

    /**
     * Weighted Macro-A* solver running directly against the real QuadraxEngine.
     */
    private fun solveLevelMacroAStar(
        levelId: Int,
        weight: Int = 3,
        maxMacroStates: Int = 25_000
    ): SolveResult? {
        val engine = QuadraxEngine(
            initialLevelId = levelId,
            progressRepo = FakeProgressStore(),
            soundManager = NoOpSoundEffects()
        )
        val def = engine.levelDef

        val spikePositions = mutableListOf<GridPosition>()
        for (y in 0..10) {
            for (x in 0..15) {
                if (engine.getBaseTile(x, y) == '^') spikePositions.add(GridPosition(x, y))
            }
        }

        val startState = engine.exportCompactState()
        val bestG = HashMap<QuadraxEngine.CompactState, Int>()
        val parentMap = HashMap<QuadraxEngine.CompactState, Pair<QuadraxEngine.CompactState?, List<MoveAction>>>()
        bestG[startState] = 0
        parentMap[startState] = Pair(null, emptyList())

        val open = PriorityQueue<QueueNode>()
        open.add(QueueNode(startState, 0, weight * heuristic(engine, startState, spikePositions)))

        val moveTypes = listOf(ActionType.LEFT, ActionType.RIGHT, ActionType.UP, ActionType.DOWN, ActionType.LEVER)

        var macroExplored = 0
        while (open.isNotEmpty() && macroExplored < maxMacroStates) {
            val node = open.poll()
            val baseState = node.state
            if (node.gSteps > (bestG[baseState] ?: Int.MAX_VALUE)) continue
            macroExplored++

            engine.importCompactState(baseState)
            val baseWorld = worldSig(engine, baseState)

            fun reconstructMacro(finalBase: QuadraxEngine.CompactState, tailMoves: List<MoveAction>): List<MoveAction> {
                val chunks = mutableListOf(tailMoves)
                var cur: QuadraxEngine.CompactState? = finalBase
                while (cur != null) {
                    val (parent, moves) = parentMap[cur]!!
                    if (moves.isNotEmpty()) chunks.add(moves)
                    cur = parent
                }
                chunks.reverse()
                return chunks.flatten()
            }

            for (expIdx in 0..1) {
                data class MicroKey(val x: Int, val y: Int, val tp: Boolean)

                val startMicro = if (expIdx == 0) {
                    MicroKey(baseState.rX, baseState.rY, baseState.rTp)
                } else {
                    MicroKey(baseState.bX, baseState.bY, baseState.bTp)
                }

                val microVisited = HashMap<MicroKey, Pair<MicroKey?, MoveAction?>>()
                microVisited[startMicro] = Pair(null, null)
                val microStateMap = HashMap<MicroKey, QuadraxEngine.CompactState>()
                microStateMap[startMicro] = baseState

                val microQueue = ArrayDeque<MicroKey>()
                microQueue.add(startMicro)

                fun reconstructMicro(endKey: MicroKey, lastAction: MoveAction): List<MoveAction> {
                    val list = mutableListOf(lastAction)
                    var cur: MicroKey? = endKey
                    while (cur != null) {
                        val (p, a) = microVisited[cur]!!
                        if (a != null) list.add(a)
                        cur = p
                    }
                    list.reverse()
                    return list
                }

                fun considerMacro(nextFull: QuadraxEngine.CompactState, moves: List<MoveAction>) {
                    val nextG = node.gSteps + moves.size
                    if (nextG < (bestG[nextFull] ?: Int.MAX_VALUE)) {
                        bestG[nextFull] = nextG
                        parentMap[nextFull] = Pair(baseState, moves)
                        val f = nextG + weight * heuristic(engine, nextFull, spikePositions)
                        open.add(QueueNode(nextFull, nextG, f))
                    }
                }

                while (microQueue.isNotEmpty()) {
                    val curMicro = microQueue.removeFirst()
                    val curFull = microStateMap[curMicro]!!

                    for (mt in moveTypes) {
                        val action = MoveAction(expIdx, mt)
                        engine.importCompactState(curFull)
                        val prevSteps = engine.steps
                        applyAction(engine, action)

                        if (engine.steps == prevSteps) continue
                        if (engine.gameOver) continue

                        val nextFull = engine.exportCompactState()
                        if (engine.won) {
                            val tail = reconstructMicro(curMicro, action)
                            return SolveResult(reconstructMacro(baseState, tail), macroExplored)
                        }

                        val nextWorld = worldSig(engine, nextFull)
                        val nx = if (expIdx == 0) nextFull.rX else nextFull.bX
                        val ny = if (expIdx == 0) nextFull.rY else nextFull.bY
                        val ntp = if (expIdx == 0) nextFull.rTp else nextFull.bTp

                        val worldChanged = nextWorld != baseWorld
                        val onExit = (nx == def.exitPos.x && ny == def.exitPos.y)
                        val onElevator = engine.elevators.any { it.x == nx && it.y - 1 == ny }
                        val livingStopperSpot = engine.boulders.any { b -> b.y == ny && (b.x == nx - 1 || b.x == nx + 1) }
                        val nextToTeleport = def.teleports.any { tp ->
                            (tp.y1 == ny && (tp.x1 == nx - 1 || tp.x1 == nx + 1)) ||
                            (tp.y2 == ny && (tp.x2 == nx - 1 || tp.x2 == nx + 1))
                        }
                        val nearLever = def.levers.values.any { l -> l.y == ny && abs(l.x - nx) == 1 }
                        val nextToGate = def.gates.values.any { g -> g.y == ny && abs(g.x - nx) == 1 }

                        if (worldChanged) {
                            considerMacro(nextFull, reconstructMicro(curMicro, action))
                        } else {
                            if (onExit || onElevator || livingStopperSpot || nextToTeleport || nearLever || nextToGate) {
                                considerMacro(nextFull, reconstructMicro(curMicro, action))
                            }
                            val nextMicro = MicroKey(nx, ny, ntp)
                            if (!microVisited.containsKey(nextMicro)) {
                                microVisited[nextMicro] = Pair(curMicro, action)
                                microStateMap[nextMicro] = nextFull
                                microQueue.add(nextMicro)
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    @Test
    fun allLevelsHaveValidGridDimensions() {
        for (id in 1..LevelRepository.TOTAL_LEVELS) {
            val def = LevelRepository.getLevel(id)
            assertEquals("Level $id id mismatch", id, def.id)
            assertEquals("Level $id must have 11 rows", 11, def.grid.size)
            def.grid.forEachIndexed { rowIdx, row ->
                assertEquals("Level $id row $rowIdx must have 16 cols", 16, row.length)
            }
        }
    }

    @Test
    fun allTwentyLevelsAreSolvableAndReplayable() {
        val summary = StringBuilder("\n=== QUADRAX MACRO-A* SOLVER VERIFICATION ===\n")
        val failedLevels = mutableListOf<String>()

        for (id in 1..LevelRepository.TOTAL_LEVELS) {
            val def = LevelRepository.getLevel(id)
            val result = solveLevelMacroAStar(id, weight = 3, maxMacroStates = 20_000)
                ?: solveLevelMacroAStar(id, weight = 6, maxMacroStates = 25_000)
                ?: solveLevelMacroAStar(id, weight = 1, maxMacroStates = 30_000)

            if (result == null) {
                failedLevels.add("L$id (${def.title})")
                summary.append(String.format("L%02d | %-20s | UNSOLVED!\n", id, def.title))
                continue
            }

            // Replay exact winning sequence on a fresh engine instance!
            val replayEngine = QuadraxEngine(
                initialLevelId = id,
                progressRepo = FakeProgressStore(),
                soundManager = NoOpSoundEffects()
            )
            var leverFlips = 0
            for (move in result.moves) {
                if (move.type == ActionType.LEVER) leverFlips++
                applyAction(replayEngine, move)
                assertFalse(
                    "Level $id died during replay at step ${replayEngine.steps}: ${replayEngine.deathReasonText}",
                    replayEngine.gameOver
                )
            }
            assertTrue("Level $id did not reach won state on replay!", replayEngine.won)
            assertEquals("Level $id step count mismatch", result.moves.size, replayEngine.steps)

            // Verify Undo works cleanly from mid-solution!
            val undoEngine = QuadraxEngine(
                initialLevelId = id,
                progressRepo = FakeProgressStore(),
                soundManager = NoOpSoundEffects()
            )
            val half = (result.moves.size / 2).coerceAtLeast(1)
            for (i in 0 until half) applyAction(undoEngine, result.moves[i])
            val midState = undoEngine.exportCompactState()
            applyAction(undoEngine, result.moves[half])
            undoEngine.undo()
            assertEquals("Level $id Undo failed to restore exact state!", midState, undoEngine.exportCompactState())

            summary.append(
                String.format(
                    "L%02d | %-20s | steps=%3d | levers=%d | broken=%d | macroStates=%5d\n",
                    id, def.title, result.moves.size, leverFlips, replayEngine.brokenTiles.size, result.macroStatesExplored
                )
            )
        }
        println(summary.toString())
        assertTrue("Unsolved levels: $failedLevels\n$summary", failedLevels.isEmpty())
    }
}
