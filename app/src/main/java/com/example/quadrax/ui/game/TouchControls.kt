package com.example.quadrax.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quadrax.data.manager.LevelRepository
import com.example.quadrax.engine.QuadraxEngine

@Composable
fun CompactGameHeader(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF14110F),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF3A312B))
    ) {
        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)) {
            // Row 1: Level picker | Rick & Ben | Steps (strictly single-line, zero vertical wrap)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LevelBadgeButton(engine = engine)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExplorerChip(
                        name = "Rick",
                        dotColor = Color(0xFF38BDF8),
                        activeBg = Color(0xFF0C4A6E),
                        activeText = Color(0xFFBAE6FD),
                        active = engine.activeIdx == 0,
                        onClick = { engine.selectExplorer(0) }
                    )
                    ExplorerChip(
                        name = "Ben",
                        dotColor = Color(0xFFF43F5E),
                        activeBg = Color(0xFF4C0519),
                        activeText = Color(0xFFFECDD3),
                        active = engine.activeIdx == 1,
                        onClick = { engine.selectExplorer(1) }
                    )
                }

                Text(
                    text = "👣 ${engine.steps}",
                    color = Color(0xFFFBBF24),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row 2: Compact Action Buttons (strictly single-line)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoneButton(
                    text = "↩ Zpět",
                    enabled = engine.canUndo(),
                    onClick = { engine.undo() },
                    modifier = Modifier.weight(1f)
                )
                StoneButton(
                    text = "🔄 Reset",
                    onClick = { engine.restartLevel() },
                    modifier = Modifier.weight(1f)
                )
                StoneButton(
                    text = if (engine.progressRepo.isSoundMuted()) "🔇" else "🔊",
                    onClick = { engine.toggleSoundMute() },
                    modifier = Modifier.weight(0.65f)
                )
                StoneButton(
                    text = "❓ Tip",
                    onClick = { engine.isWalkthroughVisible = true },
                    modifier = Modifier.weight(0.95f)
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Row 3: Slim 1-line truncated status strip (tap to read full hint in dialog)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF090807))
                    .border(1.dp, Color(0xFF26201C), RoundedCornerShape(4.dp))
                    .clickable { engine.isWalkthroughVisible = true }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "📜 ${engine.statusLog}",
                    color = Color(0xFFD6CEC3),
                    fontSize = 10.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun LandscapeLeftPanel(
    engine: QuadraxEngine,
    dpadSize: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top info card: Level + Steps
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF14110F),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF3A312B))
        ) {
            Column(
                modifier = Modifier.padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LevelBadgeButton(engine = engine, modifier = Modifier.fillMaxWidth())
                Text(
                    text = engine.levelDef.title,
                    color = Color(0xFFE7E0D6),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "👣 ${engine.steps}",
                    color = Color(0xFFFBBF24),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // Bottom: Clean D-Pad
        val fontSize = if (dpadSize < 38.dp) 14.sp else 17.sp
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DPadButton(text = "▲", onClick = { engine.handleMove(0, -1) }, size = dpadSize, fontSize = fontSize)
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                DPadButton(text = "◀", onClick = { engine.handleMove(-1, 0) }, size = dpadSize, fontSize = fontSize)
                DPadButton(text = "▼", onClick = { engine.handleMove(0, 1) }, size = dpadSize, fontSize = fontSize)
                DPadButton(text = "▶", onClick = { engine.handleMove(1, 0) }, size = dpadSize, fontSize = fontSize)
            }
        }
    }
}

@Composable
fun LandscapeRightPanel(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top: 2x2 icon utility buttons (never wrap!)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF14110F),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF3A312B))
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StoneButton(
                        text = "↩",
                        enabled = engine.canUndo(),
                        onClick = { engine.undo() },
                        modifier = Modifier.weight(1f)
                    )
                    StoneButton(
                        text = "🔄",
                        onClick = { engine.restartLevel() },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StoneButton(
                        text = if (engine.progressRepo.isSoundMuted()) "🔇" else "🔊",
                        onClick = { engine.toggleSoundMute() },
                        modifier = Modifier.weight(1f)
                    )
                    StoneButton(
                        text = "❓",
                        onClick = { engine.isWalkthroughVisible = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Middle: Rick / Ben quick selector chips stacked cleanly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ExplorerChip(
                name = "Rick",
                dotColor = Color(0xFF38BDF8),
                activeBg = Color(0xFF0C4A6E),
                activeText = Color(0xFFBAE6FD),
                active = engine.activeIdx == 0,
                onClick = { engine.selectExplorer(0) },
                modifier = Modifier.weight(1f)
            )
            ExplorerChip(
                name = "Ben",
                dotColor = Color(0xFFF43F5E),
                activeBg = Color(0xFF4C0519),
                activeText = Color(0xFFFECDD3),
                active = engine.activeIdx == 1,
                onClick = { engine.selectExplorer(1) },
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom: Primary Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { engine.switchExplorer() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0369A1)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "🔀 PŘEPNOUT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }

            Button(
                onClick = { engine.triggerAction() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⚙️ PÁKA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
private fun LevelBadgeButton(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF28231F))
            .border(1.dp, Color(0xFFD99B26), RoundedCornerShape(6.dp))
            .clickable { engine.isLevelSelectVisible = true }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LVL ${engine.levelDef.id}/${LevelRepository.TOTAL_LEVELS} ▼",
            color = Color(0xFFFACC15),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
private fun ExplorerChip(
    name: String,
    dotColor: Color,
    activeBg: Color,
    activeText: Color,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (active) activeBg else Color(0xFF1C1917))
            .border(1.dp, if (active) dotColor else Color(0xFF44403C), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = name,
            color = if (active) activeText else Color(0xFFA8A29E),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
fun DPadTouchControls(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val dpadSize = if (compact) 40.dp else 50.dp
    val fontSize = if (compact) 16.sp else 19.sp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Directional Pad
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DPadButton(text = "▲", onClick = { engine.handleMove(0, -1) }, size = dpadSize, fontSize = fontSize)
            Spacer(modifier = Modifier.height(3.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                DPadButton(text = "◀", onClick = { engine.handleMove(-1, 0) }, size = dpadSize, fontSize = fontSize)
                DPadButton(text = "▼", onClick = { engine.handleMove(0, 1) }, size = dpadSize, fontSize = fontSize)
                DPadButton(text = "▶", onClick = { engine.handleMove(1, 0) }, size = dpadSize, fontSize = fontSize)
            }
        }

        // Character Switch & Lever Action Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { engine.switchExplorer() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0369A1)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(if (compact) 118.dp else 136.dp)
                    .height(if (compact) 38.dp else 46.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "🔀 PŘEPNOUT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }

            Button(
                onClick = { engine.triggerAction() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(if (compact) 118.dp else 136.dp)
                    .height(if (compact) 38.dp else 46.dp),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⚙️ PÁKA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
fun StoneButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3B322C),
            disabledContainerColor = Color(0xFF26201C)
        ),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        modifier = modifier.height(28.dp)
    ) {
        Text(
            text = text,
            color = if (enabled) Color(0xFFF3ECE2) else Color(0xFF78675A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
fun DPadButton(
    text: String,
    onClick: () -> Unit,
    size: Dp = 48.dp,
    fontSize: TextUnit = 18.sp
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28231F)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(size)
    ) {
        Text(
            text = text,
            color = Color(0xFFFACC15),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun DeathDialog(
    engine: QuadraxEngine
) {
    if (engine.gameOver) {
        Dialog(onDismissRequest = {}) {
            Surface(
                color = Color(0xFF181412),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color(0xFF991B1B))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "💀", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "SMRTELNÝ PÁD!", color = Color(0xFFF87171), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = engine.deathReasonText,
                        color = Color(0xFFD6D3D1),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { engine.undo() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD99B26))
                        ) {
                            Text(text = "↩ Krok zpět", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                        Button(
                            onClick = { engine.restartLevel() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B322C))
                        ) {
                            Text(text = "🔄 Restart", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VictoryDialog(
    engine: QuadraxEngine
) {
    if (engine.won) {
        Dialog(onDismissRequest = {}) {
            Surface(
                color = Color(0xFF181412),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color(0xFFFACC15))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏆", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ÚROVEŇ DOKONČENA!", color = Color(0xFFFACC15), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF14110F), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "TVÉ KROKY", color = Color(0xFFA8A29E), fontSize = 10.sp)
                            Text(text = "${engine.steps}", color = Color(0xFFFACC15), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "NEJLEPŠÍ SKÓRE", color = Color(0xFFA8A29E), fontSize = 10.sp)
                            val best = engine.progressRepo.getBestSteps(engine.levelDef.id)
                            Text(text = if (best < 9999) "$best" else "-", color = Color(0xFF34D399), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                engine.soundManager.stopVictorySong()
                                engine.isLevelSelectVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B322C))
                        ) {
                            Text(text = "Levely", color = Color.White, fontSize = 12.sp, maxLines = 1, softWrap = false)
                        }
                        Button(
                            onClick = {
                                engine.soundManager.stopVictorySong()
                                engine.loadNextLevel()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD99B26))
                        ) {
                            Text(text = "DALŠÍ LEVEL", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WalkthroughDialog(
    engine: QuadraxEngine
) {
    if (engine.isWalkthroughVisible) {
        Dialog(onDismissRequest = { engine.isWalkthroughVisible = false }) {
            Surface(
                color = Color(0xFF181412),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF574A40))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NÁPOVĚDA • L${engine.levelDef.id}: ${engine.levelDef.title}",
                            color = Color(0xFFFACC15),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            color = Color.White,
                            modifier = Modifier.clickable { engine.isWalkthroughVisible = false },
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = engine.levelDef.log, color = Color(0xFFFEF08A), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    engine.levelDef.walkthrough.forEachIndexed { idx, step ->
                        Text(
                            text = "${idx + 1}. $step",
                            color = Color(0xFFD6D3D1),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelGridDialog(
    engine: QuadraxEngine
) {
    if (engine.isLevelSelectVisible) {
        Dialog(onDismissRequest = { engine.isLevelSelectVisible = false }) {
            Surface(
                color = Color(0xFF0F0D0B),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color(0xFF4A3F37)),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VÝBĚR ÚROVNĚ",
                            color = Color(0xFFFACC15),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { engine.toggleGodMode() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (engine.godMode) Color(0xFF991B1B) else Color(0xFF28231F)
                                ),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = if (engine.godMode) "GOD: ON" else "GOD: OFF",
                                    color = if (engine.godMode) Color.White else Color(0xFFF87171),
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "✕",
                                color = Color.White,
                                modifier = Modifier.clickable { engine.isLevelSelectVisible = false },
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val highestUnlocked = engine.progressRepo.getHighestUnlockedLevel()
                    val levelsList = (1..LevelRepository.TOTAL_LEVELS).toList()

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(levelsList) { lvlId ->
                            val isUnlocked = engine.godMode || lvlId <= highestUnlocked
                            val lvlDef = LevelRepository.getLevel(lvlId)
                            val best = engine.progressRepo.getBestSteps(lvlId)

                            Surface(
                                color = if (isUnlocked) Color(0xFF1E1B18) else Color(0xFF120F0D),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isUnlocked) Color(0xFF574A40) else Color(0xFF26201C)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = isUnlocked) {
                                        engine.loadLevelById(lvlId)
                                        engine.isLevelSelectVisible = false
                                    }
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "LEVEL $lvlId",
                                            color = Color(0xFFFBBF24),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                        Text(text = if (isUnlocked) "🔓" else "🔒", fontSize = 12.sp)
                                    }
                                    Text(
                                        text = lvlDef.title,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Best: ${if (best < 9999) best else "-"}",
                                        color = Color(0xFF34D399),
                                        fontSize = 10.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
