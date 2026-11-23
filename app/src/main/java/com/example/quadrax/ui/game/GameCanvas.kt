package com.example.quadrax.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.quadrax.data.model.BoulderState
import com.example.quadrax.data.model.ElevatorConfig
import com.example.quadrax.data.model.ExplorerState
import com.example.quadrax.data.model.GateConfig
import com.example.quadrax.data.model.LeverConfig
import com.example.quadrax.data.model.PlateConfig
import com.example.quadrax.data.model.TrapdoorConfig
import com.example.quadrax.engine.QuadraxEngine
import kotlin.math.sin

private const val COLS = 16
private const val ROWS = 11

@Composable
fun GameCanvas(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                engine.updateAnimations()
            }
        }
    }

    Box(
        modifier = modifier
            .background(Color(0xFF090807))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Read tickCount to register State dependency so Canvas redraws continuously!
            val tick = engine.tickCount

            val tileSize = minOf(size.width / COLS, size.height / ROWS)

            withTransform({
                if (engine.screenShake > 0f) {
                    val shakeX = ((Math.random() - 0.5) * engine.screenShake).toFloat()
                    val shakeY = ((Math.random() - 0.5) * engine.screenShake).toFloat()
                    translate(left = shakeX, top = shakeY)
                }
            }) {
                // Base grid tiles
                for (y in 0 until ROWS) {
                    for (x in 0 until COLS) {
                        val tile = engine.getBaseTile(x, y)
                        when {
                            tile == '#' -> drawWallTile(x, y, tileSize)
                            tile == '^' -> drawSpikesTile(x, y, tileSize)
                            tile == '~' && engine.isCrumblingIntact(x, y) -> {
                                val loaded = y > 0 && (engine.getExplorerAt(x, y - 1) != null || engine.getBoulderAt(x, y - 1) != null)
                                drawCrumblingTile(x, y, loaded, tick, tileSize)
                            }
                            else -> drawCaveTile(x, y, tileSize)
                        }
                    }
                }

                // Elevator Shaft Rails (behind ladders/platforms)
                for (el in engine.elevators) {
                    drawElevatorShaft(el, tileSize)
                }

                // Torches
                for (torch in engine.levelDef.torches) {
                    drawTorch(torch.x, torch.y, tick, tileSize)
                }

                // Ladders
                for (y in 0 until ROWS) {
                    for (x in 0 until COLS) {
                        if (engine.getBaseTile(x, y) == 'H') {
                            drawLadder(x, y, tileSize)
                        }
                    }
                }

                // Exit portal
                val exit = engine.levelDef.exitPos
                drawExitPortal(exit.x, exit.y, tileSize)

                // Plates
                for ((_, plate) in engine.plates) {
                    drawPlate(plate, tileSize)
                }

                // Levers
                for ((_, lever) in engine.levers) {
                    drawLever(lever, tileSize)
                }

                // Boulders
                for (boulder in engine.boulders) {
                    drawBoulder(boulder, tileSize)
                }

                // Elevators (platforms)
                for (el in engine.elevators) {
                    drawElevatorPlatform(el, tileSize)
                }

                // Gates
                val activeChannels = engine.getActiveChannels()
                for ((_, gate) in engine.gates) {
                    drawGate(gate, activeChannels, tileSize)
                }

                // Trapdoors
                for ((_, trapdoor) in engine.trapdoors) {
                    drawTrapdoor(trapdoor, tileSize)
                }

                // Teleports
                for (tp in engine.levelDef.teleports) {
                    drawTeleport(tp.x1, tp.y1, parseHexColor(tp.colorHex), tp.allowBoulders, tick, tileSize)
                    drawTeleport(tp.x2, tp.y2, parseHexColor(tp.colorHex), tp.allowBoulders, tick, tileSize)
                }

                // Explorers (inactive first, active second)
                val inactiveIdx = 1 - engine.activeIdx
                if (inactiveIdx in engine.explorers.indices) {
                    drawExplorer(engine.explorers[inactiveIdx], isActive = false, tick = tick, tileSize = tileSize, textMeasurer = textMeasurer)
                }
                if (engine.activeIdx in engine.explorers.indices) {
                    drawExplorer(engine.explorers[engine.activeIdx], isActive = true, tick = tick, tileSize = tileSize, textMeasurer = textMeasurer)
                }
            }

            // CRT Scanline Overlay
            if (engine.crtEnabled) {
                val scanlineHeight = 4f
                var y = 0f
                while (y < size.height) {
                    drawRect(
                        color = Color.Black.copy(alpha = 0.15f),
                        topLeft = Offset(0f, y),
                        size = Size(size.width, scanlineHeight / 2f)
                    )
                    y += scanlineHeight
                }
            }
        }
    }
}

private fun DrawScope.drawCaveTile(x: Int, y: Int, tileSize: Float) {
    val px = x * tileSize
    val py = y * tileSize
    val seed = (x * 37 + y * 13) % 7
    val bgColor = if (seed % 2 == 0) Color(0xFF12100E) else Color(0xFF151210)

    drawRect(color = bgColor, topLeft = Offset(px, py), size = Size(tileSize, tileSize))
    drawRect(
        color = Color(0xFF1C1815),
        topLeft = Offset(px, py),
        size = Size(tileSize, tileSize / 2f),
        style = Stroke(1f)
    )
    drawRect(
        color = Color(0xFF1C1815),
        topLeft = Offset(px, py + tileSize / 2f),
        size = Size(tileSize, tileSize / 2f),
        style = Stroke(1f)
    )
}

private fun DrawScope.drawWallTile(x: Int, y: Int, tileSize: Float) {
    val px = x * tileSize
    val py = y * tileSize
    val seed = (x * 19 + y * 31) % 11
    val baseColor = when {
        seed % 3 == 0 -> Color(0xFF3D332A)
        seed % 2 == 0 -> Color(0xFF362D25)
        else -> Color(0xFF42372D)
    }

    drawRect(color = baseColor, topLeft = Offset(px, py), size = Size(tileSize, tileSize))
    // Highlight top & left bevels
    drawRect(color = Color(0xFF5C4D3F), topLeft = Offset(px, py), size = Size(tileSize, 3f))
    drawRect(color = Color(0xFF5C4D3F), topLeft = Offset(px, py), size = Size(3f, tileSize))
    // Shadow bottom & right bevels
    drawRect(color = Color(0xFF1E1914), topLeft = Offset(px, py + tileSize - 4f), size = Size(tileSize, 4f))
    drawRect(color = Color(0xFF1E1914), topLeft = Offset(px + tileSize - 4f, py), size = Size(4f, tileSize))
    // Inner brick line
    drawRect(color = Color(0xFF2A221C), topLeft = Offset(px + 6f, py + 6f), size = Size(tileSize - 12f, 2f))
    drawRect(color = Color(0xFF2A221C), topLeft = Offset(px + 6f, py + 6f), size = Size(2f, tileSize - 12f))
}

private fun DrawScope.drawSpikesTile(x: Int, y: Int, tileSize: Float) {
    drawCaveTile(x, y, tileSize)
    val px = x * tileSize
    val py = y * tileSize
    val spikeColor = Color(0xFF94A3B8)
    val rustColor = Color(0xFF7F1D1D)

    for (i in 0 until 3) {
        val path = Path().apply {
            moveTo(px + (4f + i * 14f) * (tileSize / 48f), py + tileSize)
            lineTo(px + (11f + i * 14f) * (tileSize / 48f), py + tileSize - 20f * (tileSize / 48f))
            lineTo(px + (18f + i * 14f) * (tileSize / 48f), py + tileSize)
            close()
        }
        drawPath(path, spikeColor)

        val tipPath = Path().apply {
            moveTo(px + (9f + i * 14f) * (tileSize / 48f), py + tileSize - 12f * (tileSize / 48f))
            lineTo(px + (11f + i * 14f) * (tileSize / 48f), py + tileSize - 20f * (tileSize / 48f))
            lineTo(px + (13f + i * 14f) * (tileSize / 48f), py + tileSize - 12f * (tileSize / 48f))
            close()
        }
        drawPath(tipPath, rustColor)
    }
}

private fun DrawScope.drawTorch(x: Int, y: Int, tick: Long, tileSize: Float) {
    val cx = x * tileSize + tileSize / 2f
    val cy = y * tileSize + tileSize / 2f + 4f
    val flicker = sin(tick * 0.22f + x.toFloat()) * 4f
    val lightRadius = (72f + flicker) * (tileSize / 48f)

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFBBF24).copy(alpha = 0.28f), Color.Transparent),
            center = Offset(cx, cy - 8f),
            radius = lightRadius
        ),
        center = Offset(cx, cy - 8f),
        radius = lightRadius
    )

    drawRect(
        color = Color(0xFFEA580C),
        topLeft = Offset(cx - 3f * (tileSize / 48f), cy - 8f * (tileSize / 48f)),
        size = Size(6f * (tileSize / 48f), 10f * (tileSize / 48f))
    )
    drawRect(
        color = Color(0xFFFACC15),
        topLeft = Offset(cx - 1f * (tileSize / 48f), cy - 6f * (tileSize / 48f)),
        size = Size(2f * (tileSize / 48f), 6f * (tileSize / 48f))
    )
}

private fun DrawScope.drawLadder(x: Int, y: Int, tileSize: Float) {
    val px = x * tileSize
    val py = y * tileSize

    drawRect(
        color = Color(0xFF78350F),
        topLeft = Offset(px + 9f * (tileSize / 48f), py),
        size = Size(5f * (tileSize / 48f), tileSize)
    )
    drawRect(
        color = Color(0xFF78350F),
        topLeft = Offset(px + tileSize - 14f * (tileSize / 48f), py),
        size = Size(5f * (tileSize / 48f), tileSize)
    )

    var r = 6f
    while (r < 48f) {
        drawRect(
            color = Color(0xFFD97706),
            topLeft = Offset(px + 11f * (tileSize / 48f), py + r * (tileSize / 48f)),
            size = Size(tileSize - 22f * (tileSize / 48f), 3f * (tileSize / 48f))
        )
        r += 12f
    }
}

private fun DrawScope.drawExitPortal(x: Int, y: Int, tileSize: Float) {
    val px = x * tileSize
    val py = y * tileSize

    drawRect(
        color = Color(0xFF082F49),
        topLeft = Offset(px + 11f * (tileSize / 48f), py + 9f * (tileSize / 48f)),
        size = Size(tileSize - 22f * (tileSize / 48f), tileSize - 9f * (tileSize / 48f))
    )
    drawRect(
        color = Color(0xFF38BDF8),
        topLeft = Offset(px + 13f * (tileSize / 48f), py + 11f * (tileSize / 48f)),
        size = Size(tileSize - 26f * (tileSize / 48f), tileSize - 13f * (tileSize / 48f)),
        style = Stroke(2f)
    )
}

private fun DrawScope.drawPlate(p: PlateConfig, tileSize: Float) {
    val d = if (p.active) 4f else 0f
    val px = p.x * tileSize
    val py = p.y * tileSize

    drawRect(
        color = Color(0xFF27272A),
        topLeft = Offset(px + 6f * (tileSize / 48f), py + tileSize - 5f * (tileSize / 48f)),
        size = Size(tileSize - 12f * (tileSize / 48f), 5f * (tileSize / 48f))
    )
    drawRect(
        color = parseHexColor(p.colorHex),
        topLeft = Offset(px + 10f * (tileSize / 48f), py + tileSize - (9f - d) * (tileSize / 48f)),
        size = Size(tileSize - 20f * (tileSize / 48f), (5f - d * 0.5f) * (tileSize / 48f))
    )
}

private fun DrawScope.drawLever(l: LeverConfig, tileSize: Float) {
    val cx = l.x * tileSize + tileSize / 2f
    val by = l.y * tileSize + tileSize - 4f * (tileSize / 48f)
    val angle = if (l.active) 25f else -25f

    rotate(degrees = angle, pivot = Offset(cx, by - 4f)) {
        drawRect(
            color = Color(0xFFD6D3D1),
            topLeft = Offset(cx - 2f * (tileSize / 48f), by - 28f * (tileSize / 48f)),
            size = Size(4f * (tileSize / 48f), 24f * (tileSize / 48f))
        )
        drawCircle(
            color = parseHexColor(l.colorHex),
            radius = 6f * (tileSize / 48f),
            center = Offset(cx, by - 29f * (tileSize / 48f))
        )
    }

    drawRect(
        color = Color(0xFF44403C),
        topLeft = Offset(cx - 10f * (tileSize / 48f), by - 6f * (tileSize / 48f)),
        size = Size(20f * (tileSize / 48f), 10f * (tileSize / 48f))
    )
}

private fun DrawScope.drawBoulder(b: BoulderState, tileSize: Float) {
    val px = b.drawX * tileSize
    val py = b.drawY * tileSize

    drawRect(
        color = Color(0xFF57534E),
        topLeft = Offset(px + 2f * (tileSize / 48f), py + 2f * (tileSize / 48f)),
        size = Size(tileSize - 4f * (tileSize / 48f), tileSize - 4f * (tileSize / 48f))
    )
    drawRect(
        color = Color(0xFF3F3B37),
        topLeft = Offset(px + 9f * (tileSize / 48f), py + 9f * (tileSize / 48f)),
        size = Size(tileSize - 18f * (tileSize / 48f), tileSize - 18f * (tileSize / 48f)),
        style = Stroke(2f)
    )
}

private fun DrawScope.drawCrumblingTile(x: Int, y: Int, loaded: Boolean, tick: Long, tileSize: Float) {
    val s = tileSize / 48f
    val jitter = if (loaded) sin(tick * 0.9f) * 1.4f * s else 0f
    val px = x * tileSize + jitter
    val py = y * tileSize

    // Weathered sandstone base (distinct from solid dark brick walls)
    val baseColor = if (loaded) Color(0xFF92400E) else Color(0xFF785A3C)
    drawRect(color = baseColor, topLeft = Offset(px, py), size = Size(tileSize, tileSize))
    drawRect(color = Color(0xFFB48C64), topLeft = Offset(px, py), size = Size(tileSize, 3f * s))
    drawRect(color = Color(0xFF3E2C1C), topLeft = Offset(px, py + tileSize - 4f * s), size = Size(tileSize, 4f * s))

    // Visible fissure cracks
    val crackColor = if (loaded) Color(0xFFFDE047) else Color(0xFF1C140E)
    val crackPath = Path().apply {
        moveTo(px + 10f * s, py + 2f * s)
        lineTo(px + 18f * s, py + 18f * s)
        lineTo(px + 12f * s, py + 32f * s)
        lineTo(px + 20f * s, py + 46f * s)
        moveTo(px + 18f * s, py + 18f * s)
        lineTo(px + 34f * s, py + 24f * s)
        lineTo(px + 42f * s, py + 12f * s)
        moveTo(px + 34f * s, py + 24f * s)
        lineTo(px + 38f * s, py + 42f * s)
    }
    drawPath(crackPath, color = crackColor, style = Stroke(width = (if (loaded) 2.6f else 2.2f) * s))
}

private fun DrawScope.drawElevatorShaft(el: ElevatorConfig, tileSize: Float) {
    val s = tileSize / 48f
    val px = el.x * tileSize
    val topPx = el.topY * tileSize
    val heightPx = (el.bottomY - el.topY + 1) * tileSize

    // Vertical iron rails
    drawRect(
        color = Color(0xFF292524),
        topLeft = Offset(px + 6f * s, topPx),
        size = Size(4f * s, heightPx)
    )
    drawRect(
        color = Color(0xFF292524),
        topLeft = Offset(px + tileSize - 10f * s, topPx),
        size = Size(4f * s, heightPx)
    )
    // Center cable
    drawRect(
        color = parseHexColor(el.colorHex).copy(alpha = 0.28f),
        topLeft = Offset(px + tileSize / 2f - 1.5f * s, topPx),
        size = Size(3f * s, heightPx)
    )
}

private fun DrawScope.drawElevatorPlatform(el: ElevatorConfig, tileSize: Float) {
    val s = tileSize / 48f
    val px = el.x * tileSize
    val py = el.drawY * tileSize
    val accent = parseHexColor(el.colorHex)

    // Platform metallic body
    drawRect(
        color = Color(0xFF334155),
        topLeft = Offset(px + 2f * s, py + 2f * s),
        size = Size(tileSize - 4f * s, tileSize - 4f * s)
    )
    // Colored top deck trim
    drawRect(
        color = accent,
        topLeft = Offset(px + 2f * s, py + 2f * s),
        size = Size(tileSize - 4f * s, 7f * s)
    )
    // Inner bevel & rivets
    drawRect(
        color = Color(0xFF1E293B),
        topLeft = Offset(px + 7f * s, py + 13f * s),
        size = Size(tileSize - 14f * s, tileSize - 19f * s)
    )
    // Up/Down chevron symbol
    val cx = px + tileSize / 2f
    val cy = py + tileSize / 2f + 4f * s
    val chevron = Path().apply {
        moveTo(cx - 7f * s, cy - 2f * s)
        lineTo(cx, cy - 9f * s)
        lineTo(cx + 7f * s, cy - 2f * s)
        moveTo(cx - 7f * s, cy + 4f * s)
        lineTo(cx, cy + 11f * s)
        lineTo(cx + 7f * s, cy + 4f * s)
    }
    drawPath(chevron, color = accent.copy(alpha = 0.9f), style = Stroke(width = 2.2f * s))
}

private fun DrawScope.drawGate(g: GateConfig, activeChannels: Set<String>, tileSize: Float) {
    val s = tileSize / 48f
    val px = g.x * tileSize
    val py = g.y * tileSize
    val h = (tileSize - 6f * s) * (1f - g.openAnim)

    drawRect(
        color = Color(0xFF27272A),
        topLeft = Offset(px + 4f * s, py),
        size = Size(tileSize - 8f * s, 6f * s)
    )
    if (h > 2f) {
        val gateColor = parseHexColor(g.colorHex)
        drawRect(
            color = gateColor.copy(alpha = 0.88f),
            topLeft = Offset(px + 6f * s, py + 6f * s),
            size = Size(tileSize - 12f * s, h)
        )
        if (g.inverted && h > 14f * s) {
            // Subtle dark horizontal bars indicating inverted (normally open) gate
            drawRect(
                color = Color(0xFF18181B).copy(alpha = 0.65f),
                topLeft = Offset(px + 10f * s, py + 6f * s + h * 0.42f),
                size = Size(tileSize - 20f * s, 5f * s)
            )
        }
    }

    // AND-gate indicator gems on lintel
    if (g.requiredChannels.size >= 2) {
        val count = g.requiredChannels.size
        val spacing = (tileSize - 16f * s) / count
        g.requiredChannels.forEachIndexed { idx, ch ->
            val active = ch in activeChannels
            val dotColor = if (idx < g.requiredColors.size) parseHexColor(g.requiredColors[idx]) else parseHexColor(g.colorHex)
            drawCircle(
                color = if (active) dotColor else dotColor.copy(alpha = 0.25f),
                radius = 3.5f * s,
                center = Offset(px + 10f * s + spacing * idx + spacing * 0.35f, py + 3f * s)
            )
        }
    } else if (g.inverted) {
        // Small hollow ring on lintel to mark inverted gate even when open
        drawCircle(
            color = parseHexColor(g.colorHex),
            radius = 3f * s,
            center = Offset(px + tileSize / 2f, py + 3f * s),
            style = Stroke(width = 1.5f * s)
        )
    }
}

private fun DrawScope.drawTrapdoor(t: TrapdoorConfig, tileSize: Float) {
    val px = t.x * tileSize
    val py = t.y * tileSize
    val w = (tileSize / 2f) * (1f - t.openAnim)

    drawRect(
        color = Color(0xFF0A0807),
        topLeft = Offset(px, py),
        size = Size(tileSize, tileSize)
    )
    drawRect(
        color = Color(0xFF44403C),
        topLeft = Offset(px, py),
        size = Size(w, tileSize)
    )
    drawRect(
        color = Color(0xFF44403C),
        topLeft = Offset(px + tileSize - w, py),
        size = Size(w, tileSize)
    )
    if (t.openAnim < 1f) {
        drawRect(
            color = parseHexColor(t.colorHex),
            topLeft = Offset(px + w - 2f * (tileSize / 48f), py),
            size = Size(2f * (tileSize / 48f), tileSize)
        )
        drawRect(
            color = parseHexColor(t.colorHex),
            topLeft = Offset(px + tileSize - w, py),
            size = Size(2f * (tileSize / 48f), tileSize)
        )
    }
}

private fun DrawScope.drawTeleport(x: Int, y: Int, color: Color, allowBoulders: Boolean, tick: Long, tileSize: Float) {
    val s = tileSize / 48f
    val cx = x * tileSize + tileSize / 2f
    val cy = y * tileSize + tileSize / 2f
    val pulse = sin(tick * 0.15f) * 2f

    rotate(degrees = tick * 2.8f, pivot = Offset(cx, cy)) {
        drawOval(
            color = color.copy(alpha = 0.6f),
            topLeft = Offset(cx - (12f + pulse) * s, cy - 22f * s),
            size = Size((24f + pulse * 2f) * s, 44f * s)
        )
    }
    if (allowBoulders) {
        rotate(degrees = -tick * 2.0f, pivot = Offset(cx, cy)) {
            drawRect(
                color = Color.White.copy(alpha = 0.45f),
                topLeft = Offset(cx - 7f * s, cy - 7f * s),
                size = Size(14f * s, 14f * s),
                style = Stroke(width = 1.6f * s)
            )
        }
    }
}

private fun DrawScope.drawExplorer(
    e: ExplorerState,
    isActive: Boolean,
    tick: Long,
    tileSize: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val px = e.drawX * tileSize
    val py = e.drawY * tileSize
    val cx = px + tileSize / 2f

    if (!e.alive) {
        val result = textMeasurer.measure("💀", style = TextStyle(fontSize = 18.sp))
        drawText(result, topLeft = Offset(cx - result.size.width / 2f, py + tileSize - 32f * (tileSize / 48f)))
        return
    }

    if (isActive) {
        val activeColor = parseHexColor(e.colorHex)
        val path = Path().apply {
            moveTo(cx - 6f * (tileSize / 48f), py - 14f * (tileSize / 48f))
            lineTo(cx + 6f * (tileSize / 48f), py - 14f * (tileSize / 48f))
            lineTo(cx, py - 6f * (tileSize / 48f))
            close()
        }
        drawPath(path, activeColor)
    }

    val ls = if (e.stepFrame % 2 == 0) 2f else -2f
    val b = sin(tick * 0.15f + e.id * 2f) * 1.2f

    // Legs
    drawRect(
        color = Color(0xFF451A03),
        topLeft = Offset(cx - 7f * (tileSize / 48f), py + (36f + b) * (tileSize / 48f) + if (ls > 0) -2f else 0f),
        size = Size(6f * (tileSize / 48f), 10f * (tileSize / 48f))
    )
    drawRect(
        color = Color(0xFF451A03),
        topLeft = Offset(cx + 1f * (tileSize / 48f), py + (36f + b) * (tileSize / 48f) + if (ls < 0) -2f else 0f),
        size = Size(6f * (tileSize / 48f), 10f * (tileSize / 48f))
    )

    // Belt
    drawRect(
        color = Color(0xFF78350F),
        topLeft = Offset(cx - 7f * (tileSize / 48f), py + (29f + b) * (tileSize / 48f)),
        size = Size(14f * (tileSize / 48f), 8f * (tileSize / 48f))
    )

    // Body shirt
    drawRect(
        color = parseHexColor(e.colorHex),
        topLeft = Offset(cx - 8f * (tileSize / 48f), py + (16f + b) * (tileSize / 48f)),
        size = Size(16f * (tileSize / 48f), 14f * (tileSize / 48f))
    )

    // Face
    drawRect(
        color = Color(0xFFFED7AA),
        topLeft = Offset(cx - 6f * (tileSize / 48f), py + (7f + b) * (tileSize / 48f)),
        size = Size(12f * (tileSize / 48f), 10f * (tileSize / 48f))
    )

    // Hat brim & top
    drawRect(
        color = parseHexColor(e.hatHex),
        topLeft = Offset(cx - 9f * (tileSize / 48f), py + (6f + b) * (tileSize / 48f)),
        size = Size(18f * (tileSize / 48f), 3f * (tileSize / 48f))
    )
    drawRect(
        color = parseHexColor(e.hatHex),
        topLeft = Offset(cx - 6f * (tileSize / 48f), py + (2f + b) * (tileSize / 48f)),
        size = Size(12f * (tileSize / 48f), 5f * (tileSize / 48f))
    )
}

private fun parseHexColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val argb = clean.toLong(16) or (if (clean.length == 6) 0xFF000000 else 0)
    return Color(argb.toInt())
}
