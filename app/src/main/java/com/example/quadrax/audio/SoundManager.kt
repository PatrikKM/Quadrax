package com.example.quadrax.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.quadrax.data.manager.ProgressStore
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.sin

interface SoundEffects {
    fun playStep()
    fun playClimb()
    fun playPush()
    fun playThud()
    fun playSwitchChar()
    fun playLever()
    fun playTeleport()
    fun playCrumble()
    fun playElevator()
    fun playDeath()
    fun playVictory()
    fun stopVictorySong()
}

class SoundManager(private val progressRepo: ProgressStore) : SoundEffects {
    private val sampleRate = 22050

    private var currentVictoryThread: Thread? = null

    override fun playStep() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.TRIANGLE,
            freqStart = 135f,
            freqEnd = 65f,
            volStart = 0.12f,
            volEnd = 0.001f,
            durationMs = 60
        )
    }

    override fun playClimb() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.SQUARE,
            freqStart = 190f,
            freqEnd = 260f,
            volStart = 0.06f,
            volEnd = 0.001f,
            durationMs = 65
        )
    }

    override fun playPush() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.SAWTOOTH,
            freqStart = 92f,
            freqEnd = 62f,
            volStart = 0.16f,
            volEnd = 0.001f,
            durationMs = 150
        )
    }

    override fun playThud() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.SINE,
            freqStart = 110f,
            freqEnd = 28f,
            volStart = 0.32f,
            volEnd = 0.001f,
            durationMs = 240
        )
    }

    override fun playSwitchChar() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.TRIANGLE,
            freqStart = 440f,
            freqEnd = 587f,
            volStart = 0.1f,
            volEnd = 0.001f,
            durationMs = 120
        )
    }

    override fun playLever() {
        if (progressRepo.isSoundMuted()) return
        thread {
            for (i in 0..2) {
                playToneSequence(
                    type = ToneType.SQUARE,
                    freqStart = 480f,
                    freqEnd = 480f,
                    volStart = 0.1f,
                    volEnd = 0.001f,
                    durationMs = 35
                )
                try { Thread.sleep(45) } catch (_: Exception) {}
            }
        }
    }

    override fun playTeleport() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.SINE,
            freqStart = 800f,
            freqEnd = 100f,
            volStart = 0.15f,
            volEnd = 0.001f,
            durationMs = 350
        )
    }

    override fun playCrumble() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.SAWTOOTH,
            freqStart = 180f,
            freqEnd = 45f,
            volStart = 0.18f,
            volEnd = 0.001f,
            durationMs = 160
        )
    }

    override fun playElevator() {
        if (progressRepo.isSoundMuted()) return
        playToneSequence(
            type = ToneType.TRIANGLE,
            freqStart = 120f,
            freqEnd = 210f,
            volStart = 0.11f,
            volEnd = 0.001f,
            durationMs = 140
        )
    }

    override fun playDeath() {
        if (progressRepo.isSoundMuted()) return
        thread {
            val notes = listOf(330f, 293.66f, 261.63f, 196f, 130.81f)
            for (freq in notes) {
                playToneSequence(
                    type = ToneType.SAWTOOTH,
                    freqStart = freq,
                    freqEnd = freq,
                    volStart = 0.14f,
                    volEnd = 0.001f,
                    durationMs = 150
                )
                try { Thread.sleep(90) } catch (_: Exception) {}
            }
        }
    }

    override fun playVictory() {
        if (progressRepo.isSoundMuted()) return
        stopVictorySong()
        currentVictoryThread = thread {
            // Queen "We Are the Champions" melody
            val melody = listOf(
                MelodyNote(261f, 140),
                MelodyNote(329f, 140),
                MelodyNote(392f, 140),
                MelodyNote(523f, 250),
                MelodyNote(392f, 140),
                MelodyNote(523f, 450)
            )
            for (note in melody) {
                if (Thread.currentThread().isInterrupted) break
                playToneSequence(
                    type = ToneType.SQUARE,
                    freqStart = note.freq,
                    freqEnd = note.freq,
                    volStart = 0.12f,
                    volEnd = 0.001f,
                    durationMs = note.durationMs
                )
                try { Thread.sleep(note.durationMs.toLong() + 20) } catch (_: Exception) { break }
            }
        }
    }

    override fun stopVictorySong() {
        currentVictoryThread?.interrupt()
        currentVictoryThread = null
    }

    private enum class ToneType { SINE, SQUARE, TRIANGLE, SAWTOOTH }

    private data class MelodyNote(val freq: Float, val durationMs: Int)

    private fun playToneSequence(
        type: ToneType,
        freqStart: Float,
        freqEnd: Float,
        volStart: Float,
        volEnd: Float,
        durationMs: Int
    ) {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toFloat() / numSamples
            val freq = freqStart + (freqEnd - freqStart) * progress
            val vol = volStart * (1.0f - progress) + volEnd * progress

            val phase = 2.0 * PI * freq * i / sampleRate
            val sampleVal: Double = when (type) {
                ToneType.SINE -> sin(phase)
                ToneType.SQUARE -> if (sin(phase) >= 0) 1.0 else -1.0
                ToneType.TRIANGLE -> (2.0 / PI) * Math.asin(sin(phase))
                ToneType.SAWTOOTH -> (2.0 / PI) * Math.atan(Math.tan(phase / 2.0))
            }

            buffer[i] = (sampleVal * vol * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            thread {
                try {
                    Thread.sleep(durationMs.toLong() + 50)
                } catch (_: Exception) {}
                track.release()
            }
        } catch (_: Exception) {}
    }
}
