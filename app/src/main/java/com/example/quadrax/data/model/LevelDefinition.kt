package com.example.quadrax.data.model

data class LevelDefinition(
    val id: Int,
    val title: String,
    val subtitle: String,
    val grid: List<String>,
    val torches: List<TorchConfig> = emptyList(),
    val rickStart: GridPosition,
    val benStart: GridPosition,
    val boulders: List<BoulderState> = emptyList(),
    val levers: Map<String, LeverConfig> = emptyMap(),
    val plates: Map<String, PlateConfig> = emptyMap(),
    val trapdoors: Map<String, TrapdoorConfig> = emptyMap(),
    val gates: Map<String, GateConfig> = emptyMap(),
    val elevators: List<ElevatorConfig> = emptyList(),
    val teleports: List<TeleportPair> = emptyList(),
    val exitPos: GridPosition,
    val log: String,
    val walkthrough: List<String> = emptyList()
)
