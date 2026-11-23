package com.example.quadrax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.quadrax.audio.SoundManager
import com.example.quadrax.data.manager.LevelProgressRepository
import com.example.quadrax.engine.QuadraxEngine
import com.example.quadrax.ui.game.GameScreen
import com.example.quadrax.ui.theme.QuadraxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val progressRepo = LevelProgressRepository(this)
        val soundManager = SoundManager(progressRepo)
        val startLevelId = progressRepo.getHighestUnlockedLevel()

        setContent {
            val engine = remember {
                QuadraxEngine(
                    initialLevelId = startLevelId,
                    progressRepo = progressRepo,
                    soundManager = soundManager
                )
            }

            QuadraxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        engine = engine,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
