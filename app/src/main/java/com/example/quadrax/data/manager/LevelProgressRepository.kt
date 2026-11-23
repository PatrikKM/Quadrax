package com.example.quadrax.data.manager

import android.content.Context
import android.content.SharedPreferences

interface ProgressStore {
    fun getHighestUnlockedLevel(): Int
    fun unlockLevel(levelId: Int)
    fun getBestSteps(levelId: Int): Int
    fun saveBestSteps(levelId: Int, steps: Int)
    fun isSoundMuted(): Boolean
    fun setSoundMuted(muted: Boolean)
    fun isCrtEnabled(): Boolean
    fun setCrtEnabled(enabled: Boolean)
}

class LevelProgressRepository(context: Context) : ProgressStore {
    private val prefs: SharedPreferences = context.getSharedPreferences("quadrax_prefs", Context.MODE_PRIVATE)

    override fun getHighestUnlockedLevel(): Int {
        return prefs.getInt(KEY_HIGHEST_UNLOCKED, 1)
    }

    override fun unlockLevel(levelId: Int) {
        val current = getHighestUnlockedLevel()
        if (levelId > current) {
            prefs.edit().putInt(KEY_HIGHEST_UNLOCKED, levelId).apply()
        }
    }

    override fun getBestSteps(levelId: Int): Int {
        return prefs.getInt("${KEY_BEST_STEPS_PREFIX}$levelId", 9999)
    }

    override fun saveBestSteps(levelId: Int, steps: Int) {
        val currentBest = getBestSteps(levelId)
        if (steps < currentBest) {
            prefs.edit().putInt("${KEY_BEST_STEPS_PREFIX}$levelId", steps).apply()
        }
    }

    override fun isSoundMuted(): Boolean {
        return prefs.getBoolean(KEY_SOUND_MUTED, false)
    }

    override fun setSoundMuted(muted: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_MUTED, muted).apply()
    }

    override fun isCrtEnabled(): Boolean {
        return prefs.getBoolean(KEY_CRT_ENABLED, true)
    }

    override fun setCrtEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CRT_ENABLED, enabled).apply()
    }

    companion object {
        private const val KEY_HIGHEST_UNLOCKED = "highest_unlocked_level"
        private const val KEY_BEST_STEPS_PREFIX = "best_steps_lvl_"
        private const val KEY_SOUND_MUTED = "sound_muted"
        private const val KEY_CRT_ENABLED = "crt_enabled"
    }
}
