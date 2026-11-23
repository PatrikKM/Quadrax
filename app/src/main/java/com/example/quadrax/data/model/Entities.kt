package com.example.quadrax.data.model

data class GridPosition(
    val x: Int,
    val y: Int
)

data class ExplorerState(
    val id: Int,
    val name: String,
    val colorHex: String,
    val hatHex: String,
    var x: Int,
    var y: Int,
    var drawX: Float = x.toFloat(),
    var drawY: Float = y.toFloat(),
    var facing: Int = 1, // 1 for Right, -1 for Left
    var alive: Boolean = true,
    var stepFrame: Int = 0,
    var tpCooldown: Boolean = false
) {
    fun copyState(): ExplorerState = copy()
}

data class BoulderState(
    val id: Int,
    var x: Int,
    var y: Int,
    var drawX: Float = x.toFloat(),
    var drawY: Float = y.toFloat(),
    var tpCooldown: Boolean = false
) {
    fun copyState(): BoulderState = copy()
}

data class LeverConfig(
    val x: Int,
    val y: Int,
    val colorHex: String,
    val label: String,
    var active: Boolean = false,
    val channel: String = ""
) {
    fun copyState(): LeverConfig = copy()
}

data class PlateConfig(
    val x: Int,
    val y: Int,
    val colorHex: String,
    val label: String,
    var active: Boolean = false,
    val channel: String = ""
) {
    fun copyState(): PlateConfig = copy()
}

data class GateConfig(
    val x: Int,
    val y: Int,
    val colorHex: String,
    var open: Boolean = false,
    var openAnim: Float = 0f,
    val channel: String = "",
    val requiredChannels: List<String> = emptyList(),
    val requiredColors: List<String> = emptyList(),
    val inverted: Boolean = false
) {
    fun copyState(): GateConfig = copy()
}

data class TrapdoorConfig(
    val x: Int,
    val y: Int,
    val colorHex: String,
    var open: Boolean = false,
    var openAnim: Float = 0f,
    val channel: String = "",
    val requiredChannels: List<String> = emptyList(),
    val inverted: Boolean = false
) {
    fun copyState(): TrapdoorConfig = copy()
}

data class ElevatorConfig(
    val id: String,
    val x: Int,
    val bottomY: Int,
    val topY: Int,
    val channel: String,
    val colorHex: String,
    val inverted: Boolean = false,
    var y: Int = if (inverted) topY else bottomY,
    var drawY: Float = y.toFloat()
) {
    fun copyState(): ElevatorConfig = copy()
}

data class TeleportPair(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
    val colorHex: String,
    val allowBoulders: Boolean = true
)

data class TorchConfig(
    val x: Int,
    val y: Int
)
