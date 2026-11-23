package com.example.quadrax.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quadrax.audio.SoundEffects
import com.example.quadrax.data.manager.LevelRepository
import com.example.quadrax.data.manager.ProgressStore
import com.example.quadrax.data.model.*

class QuadraxEngine(
    initialLevelId: Int = 1,
    val progressRepo: ProgressStore,
    val soundManager: SoundEffects
) {
    var levelDef: LevelDefinition by mutableStateOf(LevelRepository.getLevel(initialLevelId))
        private set

    var currentLevelIdx by mutableIntStateOf(initialLevelId - 1)
        private set

    var explorers by mutableStateOf<List<ExplorerState>>(emptyList())
        private set

    var activeIdx by mutableIntStateOf(0)
        private set

    var boulders by mutableStateOf<List<BoulderState>>(emptyList())
        private set

    var levers by mutableStateOf<Map<String, LeverConfig>>(emptyMap())
        private set

    var plates by mutableStateOf<Map<String, PlateConfig>>(emptyMap())
        private set

    var trapdoors by mutableStateOf<Map<String, TrapdoorConfig>>(emptyMap())
        private set

    var gates by mutableStateOf<Map<String, GateConfig>>(emptyMap())
        private set

    var elevators by mutableStateOf<List<ElevatorConfig>>(emptyList())
        private set

    var brokenTiles by mutableStateOf<Set<GridPosition>>(emptySet())
        private set

    var steps by mutableIntStateOf(0)
        private set

    var won by mutableStateOf(false)
        private set

    var gameOver by mutableStateOf(false)
        private set

    var deathReasonText by mutableStateOf("")
        private set

    var statusLog by mutableStateOf("")
        private set

    var screenShake by mutableFloatStateOf(0f)
        private set

    var crtEnabled by mutableStateOf(progressRepo.isCrtEnabled())
        private set

    var godMode by mutableStateOf(false)
        private set

    var isWalkthroughVisible by mutableStateOf(false)

    var isLevelSelectVisible by mutableStateOf(false)

    var isSettingsVisible by mutableStateOf(false)

    var tickCount by mutableLongStateOf(0L)
        private set

    private val undoStack = mutableListOf<Snapshot>()

    internal data class CompactState(
        val rX: Int, val rY: Int, val rTp: Boolean,
        val bX: Int, val bY: Int, val bTp: Boolean,
        val boulders: List<Long>,
        val leverMask: Int,
        val elevatorYs: List<Int>,
        val brokenTiles: Set<GridPosition>
    )

    internal fun exportCompactState(): CompactState {
        var mask = 0
        var bit = 1
        for ((_, l) in levers) {
            if (l.active) mask = mask or bit
            bit = bit shl 1
        }
        val bList = boulders.map { b ->
            ((b.x.toLong() shl 16) or (b.y.toLong() shl 1) or (if (b.tpCooldown) 1L else 0L))
        }.sorted()
        return CompactState(
            rX = explorers[0].x, rY = explorers[0].y, rTp = explorers[0].tpCooldown,
            bX = explorers[1].x, bY = explorers[1].y, bTp = explorers[1].tpCooldown,
            boulders = bList,
            leverMask = mask,
            elevatorYs = elevators.map { it.y },
            brokenTiles = brokenTiles.toSet()
        )
    }

    internal fun importCompactState(s: CompactState) {
        explorers[0].x = s.rX
        explorers[0].y = s.rY
        explorers[0].tpCooldown = s.rTp
        explorers[0].alive = true
        explorers[1].x = s.bX
        explorers[1].y = s.bY
        explorers[1].tpCooldown = s.bTp
        explorers[1].alive = true

        boulders.forEachIndexed { idx, b ->
            val packed = s.boulders[idx]
            b.x = (packed shr 16).toInt()
            b.y = ((packed shr 1) and 0x7FFF).toInt()
            b.tpCooldown = (packed and 1L) != 0L
        }

        var bit = 1
        for ((_, l) in levers) {
            l.active = (s.leverMask and bit) != 0
            bit = bit shl 1
        }

        elevators.forEachIndexed { idx, el ->
            el.y = s.elevatorYs[idx]
            el.drawY = el.y.toFloat()
        }

        brokenTiles = s.brokenTiles.toSet()
        gameOver = false
        won = false
        deathReasonText = ""
        undoStack.clear()
        updateMechanisms(instant = true, moveElevators = false)
    }

    private data class Snapshot(
        val explorers: List<ExplorerState>,
        val activeIdx: Int,
        val boulders: List<BoulderState>,
        val levers: Map<String, LeverConfig>,
        val elevators: List<ElevatorConfig>,
        val brokenTiles: Set<GridPosition>,
        val steps: Int
    )

    init {
        loadLevelById(initialLevelId, playSfx = false)
    }

    fun loadLevelById(id: Int, playSfx: Boolean = true) {
        val clampedId = id.coerceIn(1, LevelRepository.TOTAL_LEVELS)
        currentLevelIdx = clampedId - 1
        levelDef = LevelRepository.getLevel(clampedId)
        restartLevel(playSfx)
    }

    fun loadLevelDefinition(def: LevelDefinition, playSfx: Boolean = false) {
        currentLevelIdx = (def.id - 1).coerceAtLeast(0)
        levelDef = def
        restartLevel(playSfx)
    }

    fun loadNextLevel() {
        if (currentLevelIdx + 1 < LevelRepository.TOTAL_LEVELS) {
            loadLevelById(currentLevelIdx + 2)
        } else {
            isLevelSelectVisible = true
        }
    }

    fun restartLevel(playSfx: Boolean = true) {
        if (playSfx) soundManager.playLever()

        explorers = listOf(
            ExplorerState(
                id = 0, name = "Rick", colorHex = "#38bdf8", hatHex = "#fde047",
                x = levelDef.rickStart.x, y = levelDef.rickStart.y
            ),
            ExplorerState(
                id = 1, name = "Ben", colorHex = "#f43f5e", hatHex = "#d97706",
                x = levelDef.benStart.x, y = levelDef.benStart.y
            )
        )
        activeIdx = 0

        boulders = levelDef.boulders.map { it.copyState() }

        levers = levelDef.levers.mapValues { it.value.copyState() }
        plates = levelDef.plates.mapValues { it.value.copyState() }
        trapdoors = levelDef.trapdoors.mapValues { it.value.copyState() }
        gates = levelDef.gates.mapValues { it.value.copyState() }
        elevators = levelDef.elevators.map {
            val startY = if (it.inverted) it.topY else it.bottomY
            it.copy(y = startY, drawY = startY.toFloat())
        }
        brokenTiles = emptySet()

        steps = 0
        undoStack.clear()
        won = false
        gameOver = false
        deathReasonText = ""
        screenShake = 0f

        updateMechanisms(instant = true, moveElevators = false)
        statusLog = levelDef.log
        notifyStateChange()
    }

    fun toggleGodMode() {
        godMode = !godMode
        statusLog = if (godMode) "God Mode: Všechny levely zpřístupněny." else "God Mode vypnut."
        notifyStateChange()
    }

    fun toggleCrt() {
        crtEnabled = !crtEnabled
        progressRepo.setCrtEnabled(crtEnabled)
        statusLog = if (crtEnabled) "CRT Filtr zapnut." else "CRT Filtr vypnut."
        notifyStateChange()
    }

    fun toggleSoundMute() {
        val newMuted = !progressRepo.isSoundMuted()
        progressRepo.setSoundMuted(newMuted)
        statusLog = if (newMuted) "Zvuk vypnut." else "Zvuk zapnut."
        notifyStateChange()
    }

    fun selectExplorer(idx: Int) {
        if (gameOver || won || activeIdx == idx) return
        activeIdx = idx
        soundManager.playSwitchChar()
        statusLog = "Aktivní: ${explorers[idx].name}"
        notifyStateChange()
    }

    fun switchExplorer() {
        selectExplorer(1 - activeIdx)
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()

    fun undo() {
        if (undoStack.isEmpty()) return
        soundManager.playStep()
        val snapshot = undoStack.removeAt(undoStack.size - 1)

        explorers = snapshot.explorers.map { it.copyState() }
        activeIdx = snapshot.activeIdx
        boulders = snapshot.boulders.map { it.copyState() }
        levers = snapshot.levers.mapValues { it.value.copyState() }
        elevators = snapshot.elevators.map { it.copyState() }
        brokenTiles = snapshot.brokenTiles.toSet()
        steps = snapshot.steps

        gameOver = false
        won = false
        deathReasonText = ""
        updateMechanisms(instant = true, moveElevators = false)
        statusLog = "Krok vrácen."
        notifyStateChange()
    }

    private fun saveSnapshot() {
        undoStack.add(
            Snapshot(
                explorers = explorers.map { it.copyState() },
                activeIdx = activeIdx,
                boulders = boulders.map { it.copyState() },
                levers = levers.mapValues { it.value.copyState() },
                elevators = elevators.map { it.copyState() },
                brokenTiles = brokenTiles.toSet(),
                steps = steps
            )
        )
        if (undoStack.size > 250) {
            undoStack.removeAt(0)
        }
    }

    fun triggerAction() {
        if (gameOver || won) return
        val e = explorers[activeIdx]
        for ((_, l) in levers) {
            if (l.x == e.x && l.y == e.y) {
                val armedCrumbling = getOccupiedCrumblingTiles()
                saveSnapshot()
                l.active = !l.active
                steps++
                soundManager.playLever()
                updateMechanisms()
                crumbleVacatedTiles(armedCrumbling)
                resolvePhysics()
                statusLog = "${e.name} přepnul páku."
                notifyStateChange()
                return
            }
        }
    }

    fun handleMove(dx: Int, dy: Int) {
        if (gameOver || won) return
        val e = explorers[activeIdx]
        if (!e.alive) return

        if (dy != 0) {
            val ty = e.y + dy
            if (!inBounds(e.x, ty) || (!isLadder(e.x, e.y) && !isLadder(e.x, ty))) return
            if (isSolidForExplorer(e.x, ty)) return

            val armedCrumbling = getOccupiedCrumblingTiles()
            saveSnapshot()
            e.y = ty
            e.stepFrame++
            steps++
            e.tpCooldown = false
            soundManager.playClimb()
            crumbleVacatedTiles(armedCrumbling)
            updateMechanisms()
            resolvePhysics()
            checkTeleports(e)
            notifyStateChange()
            return
        }

        if (dx != 0) {
            e.facing = dx
            val nx = e.x + dx
            val ny = e.y
            if (!inBounds(nx, ny)) return

            val b = getBoulderAt(nx, ny)
            if (b != null) {
                // Push boulder horizontal
                if (isExplorerSupported(e.x, e.y) && getBoulderAt(nx, ny - 1) == null && isTileEmptyForBoulder(nx + dx, ny)) {
                    val armedCrumbling = getOccupiedCrumblingTiles()
                    saveSnapshot()
                    b.x = nx + dx
                    b.tpCooldown = false
                    e.x = nx
                    e.stepFrame++
                    steps++
                    e.tpCooldown = false
                    soundManager.playPush()
                    crumbleVacatedTiles(armedCrumbling)
                    updateMechanisms()
                    resolvePhysics()
                    checkTeleports(e)
                    notifyStateChange()
                    return
                }
                // Step-up onto boulder
                if (isExplorerSupported(e.x, e.y) && !isSolidForExplorer(e.x, ny - 1) && !isSolidForExplorer(nx, ny - 1)) {
                    val armedCrumbling = getOccupiedCrumblingTiles()
                    saveSnapshot()
                    e.x = nx
                    e.y = ny - 1
                    e.stepFrame++
                    steps++
                    e.tpCooldown = false
                    soundManager.playStep()
                    crumbleVacatedTiles(armedCrumbling)
                    updateMechanisms()
                    resolvePhysics()
                    checkTeleports(e)
                    notifyStateChange()
                    return
                }
                return
            }

            // Normal step
            if (!isSolidForExplorer(nx, ny)) {
                val armedCrumbling = getOccupiedCrumblingTiles()
                saveSnapshot()
                e.x = nx
                e.stepFrame++
                steps++
                e.tpCooldown = false
                soundManager.playStep()
                crumbleVacatedTiles(armedCrumbling)
                updateMechanisms()
                resolvePhysics()
                checkTeleports(e)
                notifyStateChange()
                return
            }

            // Step-up onto solid block (wall, closed gate, crumbling tile, elevator)
            if (isExplorerSupported(e.x, e.y) && !isSolidForExplorer(e.x, ny - 1) && !isSolidForExplorer(nx, ny - 1)) {
                val armedCrumbling = getOccupiedCrumblingTiles()
                saveSnapshot()
                e.x = nx
                e.y = ny - 1
                e.stepFrame++
                steps++
                e.tpCooldown = false
                soundManager.playStep()
                crumbleVacatedTiles(armedCrumbling)
                updateMechanisms()
                resolvePhysics()
                checkTeleports(e)
                notifyStateChange()
            }
        }
    }

    private fun checkTeleports(e: ExplorerState) {
        if (!e.alive || gameOver || won) return
        for (tp in levelDef.teleports) {
            if (e.x == tp.x1 && e.y == tp.y1 && !e.tpCooldown) {
                if (isSolidForExplorer(tp.x2, tp.y2)) return
                val armedCrumbling = getOccupiedCrumblingTiles()
                e.x = tp.x2
                e.y = tp.y2
                e.drawX = tp.x2.toFloat()
                e.drawY = tp.y2.toFloat()
                e.tpCooldown = true
                soundManager.playTeleport()
                crumbleVacatedTiles(armedCrumbling)
                updateMechanisms()
                resolvePhysics()
                return
            }
            if (e.x == tp.x2 && e.y == tp.y2 && !e.tpCooldown) {
                if (isSolidForExplorer(tp.x1, tp.y1)) return
                val armedCrumbling = getOccupiedCrumblingTiles()
                e.x = tp.x1
                e.y = tp.y1
                e.drawX = tp.x1.toFloat()
                e.drawY = tp.y1.toFloat()
                e.tpCooldown = true
                soundManager.playTeleport()
                crumbleVacatedTiles(armedCrumbling)
                updateMechanisms()
                resolvePhysics()
                return
            }
        }
    }

    private fun tryTeleportSingleBoulder(b: BoulderState): Boolean {
        if (b.tpCooldown) return false
        for (tp in levelDef.teleports) {
            if (!tp.allowBoulders) continue
            if (b.x == tp.x1 && b.y == tp.y1 && isTileEmptyForBoulder(tp.x2, tp.y2)) {
                b.x = tp.x2
                b.y = tp.y2
                b.drawX = tp.x2.toFloat()
                b.drawY = tp.y2.toFloat()
                b.tpCooldown = true
                soundManager.playTeleport()
                return true
            }
            if (b.x == tp.x2 && b.y == tp.y2 && isTileEmptyForBoulder(tp.x1, tp.y1)) {
                b.x = tp.x1
                b.y = tp.y1
                b.drawX = tp.x1.toFloat()
                b.drawY = tp.y1.toFloat()
                b.tpCooldown = true
                soundManager.playTeleport()
                return true
            }
        }
        return false
    }

    fun getActiveChannels(): Set<String> {
        val active = mutableSetOf<String>()
        for ((k, l) in levers) {
            if (l.active) active.add(l.channel.ifEmpty { k })
        }
        for ((k, p) in plates) {
            if (p.active) active.add(p.channel.ifEmpty { k })
        }
        return active
    }

    fun updateMechanisms(instant: Boolean = false, moveElevators: Boolean = true): Boolean {
        for ((_, p) in plates) {
            p.active = getBoulderAt(p.x, p.y) != null || getExplorerAt(p.x, p.y) != null
        }

        val activeChannels = getActiveChannels()

        for ((k, g) in gates) {
            val triggered = if (g.requiredChannels.isNotEmpty()) {
                g.requiredChannels.all { it in activeChannels }
            } else {
                g.channel.ifEmpty { k } in activeChannels
            }
            val op = if (g.inverted) !triggered else triggered
            g.open = op
            if (instant) g.openAnim = if (op) 1f else 0f
        }

        for ((k, t) in trapdoors) {
            val triggered = if (t.requiredChannels.isNotEmpty()) {
                t.requiredChannels.all { it in activeChannels }
            } else {
                t.channel.ifEmpty { k } in activeChannels
            }
            val op = if (t.inverted) !triggered else triggered
            t.open = op
            if (instant) t.openAnim = if (op) 1f else 0f
        }

        if (!moveElevators) return false
        return stepElevators(instant)
    }

    private fun stepElevators(instant: Boolean): Boolean {
        if (elevators.isEmpty()) return false
        var anyMoved = false
        val activeChannels = getActiveChannels()

        for (el in elevators) {
            val triggered = el.channel in activeChannels
            val targetY = if (el.inverted) {
                if (triggered) el.bottomY else el.topY
            } else {
                if (triggered) el.topY else el.bottomY
            }

            var safety = 0
            while (el.y != targetY && safety < 16) {
                safety++
                if (targetY < el.y) {
                    // Moving UP
                    val ridingBoulders = mutableListOf<BoulderState>()
                    val ridingExplorers = mutableListOf<ExplorerState>()
                    var scanY = el.y - 1
                    while (inBounds(el.x, scanY)) {
                        val b = getBoulderAt(el.x, scanY)
                        val exps = explorers.filter { it.alive && it.x == el.x && it.y == scanY }
                        if (b != null) {
                            ridingBoulders.add(b)
                            scanY--
                        } else if (exps.isNotEmpty()) {
                            ridingExplorers.addAll(exps)
                            scanY--
                            break
                        } else {
                            break
                        }
                    }

                    if (!inBounds(el.x, scanY) || isBlockedForElevator(el.x, scanY, excludeElevator = el, checkExplorers = true)) {
                        break
                    }

                    for (ex in ridingExplorers) {
                        ex.y -= 1
                        ex.tpCooldown = false
                    }
                    for (b in ridingBoulders) {
                        b.y -= 1
                        b.tpCooldown = false
                    }
                    el.y -= 1
                    anyMoved = true
                } else {
                    // Moving DOWN
                    val ny = el.y + 1
                    if (!inBounds(el.x, ny) || isBlockedForElevator(el.x, ny, excludeElevator = el, checkExplorers = true)) {
                        break
                    }

                    val ridingBoulders = mutableListOf<BoulderState>()
                    val ridingExplorers = mutableListOf<ExplorerState>()
                    var scanY = el.y - 1
                    while (inBounds(el.x, scanY)) {
                        val b = getBoulderAt(el.x, scanY)
                        val exps = explorers.filter { it.alive && it.x == el.x && it.y == scanY }
                        if (b != null) {
                            ridingBoulders.add(b)
                            scanY--
                        } else if (exps.isNotEmpty()) {
                            ridingExplorers.addAll(exps)
                            scanY--
                            break
                        } else {
                            break
                        }
                    }

                    el.y += 1
                    for (b in ridingBoulders) {
                        b.y += 1
                        b.tpCooldown = false
                    }
                    for (ex in ridingExplorers) {
                        ex.y += 1
                        ex.tpCooldown = false
                    }
                    anyMoved = true
                }
            }

            if (instant) {
                el.drawY = el.y.toFloat()
            }
        }

        if (anyMoved && !instant) {
            soundManager.playElevator()
        }
        return anyMoved
    }

    private fun isBlockedForElevator(x: Int, y: Int, excludeElevator: ElevatorConfig, checkExplorers: Boolean): Boolean {
        if (!inBounds(x, y)) return true
        if (getBaseTile(x, y) == '#') return true
        if (isCrumblingIntact(x, y)) return true
        if (getClosedGateAt(x, y) != null) return true
        if (getClosedTrapdoorAt(x, y) != null) return true
        if (getBoulderAt(x, y) != null) return true
        if (elevators.any { it !== excludeElevator && it.x == x && it.y == y }) return true
        if (checkExplorers && getExplorerAt(x, y) != null) return true
        return false
    }

    private fun getOccupiedCrumblingTiles(): Set<GridPosition> {
        val occupied = mutableSetOf<GridPosition>()
        for (y in 1..10) {
            for (x in 0..15) {
                if (isCrumblingIntact(x, y)) {
                    if (getExplorerAt(x, y - 1) != null || getBoulderAt(x, y - 1) != null) {
                        occupied.add(GridPosition(x, y))
                    }
                }
            }
        }
        return occupied
    }

    private fun crumbleVacatedTiles(armed: Set<GridPosition>): Boolean {
        if (armed.isEmpty()) return false
        val newlyBroken = mutableSetOf<GridPosition>()
        for (pos in armed) {
            if (isCrumblingIntact(pos.x, pos.y)) {
                val stillOccupied = pos.y > 0 && (getExplorerAt(pos.x, pos.y - 1) != null || getBoulderAt(pos.x, pos.y - 1) != null)
                if (!stillOccupied) {
                    newlyBroken.add(pos)
                }
            }
        }
        if (newlyBroken.isNotEmpty()) {
            brokenTiles = brokenTiles + newlyBroken
            soundManager.playCrumble()
            return true
        }
        return false
    }

    private fun resolvePhysics(depth: Int = 0) {
        var anyFell = false
        var changed = true
        var iterations = 0

        while (changed && iterations < 30) {
            iterations++
            changed = false
            val iterArmed = getOccupiedCrumblingTiles()

            for (b in boulders) {
                if (tryTeleportSingleBoulder(b)) {
                    changed = true
                }
                var d = 0
                while (isTileEmptyForBoulder(b.x, b.y + 1)) {
                    b.y++
                    b.tpCooldown = false
                    d++
                    if (tryTeleportSingleBoulder(b)) {
                        changed = true
                        break
                    }
                }
                if (d > 0) {
                    anyFell = true
                    changed = true
                }
            }

            if (crumbleVacatedTiles(iterArmed)) {
                changed = true
            }
            if (updateMechanisms()) {
                changed = true
            }
        }

        if (anyFell) {
            soundManager.playThud()
            screenShake = 8f
        }

        val postBoulderArmed = getOccupiedCrumblingTiles()
        var anyExplorerMoved = false
        for (e in explorers) {
            if (!e.alive) continue
            var d = 0
            while (!isExplorerSupported(e.x, e.y)) {
                if (isSolidForExplorer(e.x, e.y + 1)) break
                e.y++
                e.tpCooldown = false
                d++
                anyExplorerMoved = true
                if (getBaseTile(e.x, e.y) == '^' && getBoulderAt(e.x, e.y) == null) break
            }

            if (getBaseTile(e.x, e.y) == '^' && getBoulderAt(e.x, e.y) == null) {
                e.alive = false
                gameOver = true
                screenShake = 12f
                soundManager.playDeath()
                deathReasonText = "${e.name} udělal osudovou chybu a napíchl se na smrtící bodáky!"
            } else if (d >= 3) {
                e.alive = false
                gameOver = true
                screenShake = 12f
                soundManager.playDeath()
                deathReasonText = "${e.name} spadl z výšky $d bloků! Přežiješ seskok nejvýše ze 2 bloků."
            } else if (d > 0) {
                soundManager.playStep()
            }
        }

        if (anyExplorerMoved) {
            crumbleVacatedTiles(postBoulderArmed)
        }

        val elevatorMovedAfterLand = updateMechanisms()
        val needsAnotherPass = !gameOver && depth < 8 && (
            elevatorMovedAfterLand ||
            boulders.any { isTileEmptyForBoulder(it.x, it.y + 1) } ||
            explorers.any { it.alive && !isExplorerSupported(it.x, it.y) && !isSolidForExplorer(it.x, it.y + 1) }
        )
        if (needsAnotherPass) {
            resolvePhysics(depth + 1)
            return
        }

        if (!gameOver && explorers.isNotEmpty() && explorers[0].alive && explorers[1].alive &&
            explorers[0].x == levelDef.exitPos.x && explorers[0].y == levelDef.exitPos.y &&
            explorers[1].x == levelDef.exitPos.x && explorers[1].y == levelDef.exitPos.y
        ) {
            won = true
            soundManager.playVictory()
            progressRepo.unlockLevel(levelDef.id + 1)
            progressRepo.saveBestSteps(levelDef.id, steps)
        }
    }

    fun updateAnimations() {
        tickCount++
        for (e in explorers) {
            e.drawX += (e.x.toFloat() - e.drawX) * 0.36f
            e.drawY += (e.y.toFloat() - e.drawY) * 0.36f
        }
        for (b in boulders) {
            b.drawX += (b.x.toFloat() - b.drawX) * 0.34f
            b.drawY += (b.y.toFloat() - b.drawY) * 0.34f
        }
        for ((_, g) in gates) {
            val target = if (g.open) 1f else 0f
            g.openAnim += (target - g.openAnim) * 0.22f
        }
        for ((_, t) in trapdoors) {
            val target = if (t.open) 1f else 0f
            t.openAnim += (target - t.openAnim) * 0.25f
        }
        for (el in elevators) {
            el.drawY += (el.y.toFloat() - el.drawY) * 0.25f
        }
        if (screenShake > 0f) {
            screenShake *= 0.82f
            if (screenShake < 0.3f) screenShake = 0f
        }
    }

    private fun notifyStateChange() {
        tickCount++
    }

    fun inBounds(x: Int, y: Int): Boolean = x in 0..15 && y in 0..10

    fun getBaseTile(x: Int, y: Int): Char {
        return if (inBounds(x, y)) levelDef.grid[y][x] else '#'
    }

    fun isCrumblingIntact(x: Int, y: Int): Boolean {
        return getBaseTile(x, y) == '~' && GridPosition(x, y) !in brokenTiles
    }

    fun isLadder(x: Int, y: Int): Boolean = getBaseTile(x, y) == 'H'

    fun getClosedGateAt(x: Int, y: Int): GateConfig? {
        for ((_, g) in gates) {
            if (g.x == x && g.y == y && !g.open) return g
        }
        return null
    }

    fun getClosedTrapdoorAt(x: Int, y: Int): TrapdoorConfig? {
        for ((_, t) in trapdoors) {
            if (t.x == x && t.y == y && !t.open) return t
        }
        return null
    }

    fun getElevatorAt(x: Int, y: Int): ElevatorConfig? {
        return elevators.find { it.x == x && it.y == y }
    }

    fun getBoulderAt(x: Int, y: Int): BoulderState? {
        return boulders.find { it.x == x && it.y == y }
    }

    fun getExplorerAt(x: Int, y: Int): ExplorerState? {
        return explorers.find { it.alive && it.x == x && it.y == y }
    }

    fun isSolidForExplorer(x: Int, y: Int): Boolean {
        return getBaseTile(x, y) == '#' ||
                isCrumblingIntact(x, y) ||
                getClosedGateAt(x, y) != null ||
                getBoulderAt(x, y) != null ||
                getClosedTrapdoorAt(x, y) != null ||
                getElevatorAt(x, y) != null
    }

    fun isExplorerSupported(x: Int, y: Int): Boolean {
        if (isLadder(x, y)) return true
        val by = y + 1
        return getBaseTile(x, by) == '#' ||
                isCrumblingIntact(x, by) ||
                getClosedGateAt(x, by) != null ||
                getBoulderAt(x, by) != null ||
                isLadder(x, by) ||
                getClosedTrapdoorAt(x, by) != null ||
                getElevatorAt(x, by) != null
    }

    fun isTileEmptyForBoulder(x: Int, y: Int): Boolean {
        if (!inBounds(x, y)) return false
        return !(getBaseTile(x, y) == '#' ||
                isCrumblingIntact(x, y) ||
                getClosedGateAt(x, y) != null ||
                getBoulderAt(x, y) != null ||
                getExplorerAt(x, y) != null ||
                getClosedTrapdoorAt(x, y) != null ||
                getElevatorAt(x, y) != null)
    }
}
