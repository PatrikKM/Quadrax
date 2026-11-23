package com.example.quadrax.ui.game

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.quadrax.engine.QuadraxEngine

@Composable
fun GameScreen(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B0A09))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        if (isLandscape) {
            // Handheld Landscape Layout:
            // Left rail (Level + D-Pad) | Huge centered 16:11 GameCanvas | Right rail (Tools + Heroes + Actions)
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val sideWidth = (maxWidth * 0.19f).coerceIn(110.dp, 146.dp)
                val dpadBtnSize = ((sideWidth - 8.dp) / 3f).coerceIn(34.dp, 46.dp)

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LandscapeLeftPanel(
                        engine = engine,
                        dpadSize = dpadBtnSize,
                        modifier = Modifier
                            .width(sideWidth)
                            .fillMaxHeight()
                    )

                    FittedGameCanvasBox(
                        engine = engine,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    LandscapeRightPanel(
                        engine = engine,
                        modifier = Modifier
                            .width(sideWidth)
                            .fillMaxHeight()
                    )
                }
            }
        } else {
            // Portrait Layout: Compact non-wrapping header top, max 16:11 canvas middle, comfortable controls bottom
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                CompactGameHeader(engine = engine)

                Spacer(modifier = Modifier.height(4.dp))

                FittedGameCanvasBox(
                    engine = engine,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                DPadTouchControls(engine = engine, compact = false)
            }
        }

        // Dialogs & Overlays
        DeathDialog(engine = engine)
        VictoryDialog(engine = engine)
        WalkthroughDialog(engine = engine)
        LevelGridDialog(engine = engine)
    }
}

@Composable
private fun FittedGameCanvasBox(
    engine: QuadraxEngine,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val ratio = 16f / 11f
        val byHeightWidth = maxHeight * ratio
        val canvasWidth = if (byHeightWidth <= maxWidth) byHeightWidth else maxWidth
        val canvasHeight = canvasWidth / ratio

        GameCanvas(
            engine = engine,
            modifier = Modifier.size(width = canvasWidth, height = canvasHeight)
        )
    }
}
