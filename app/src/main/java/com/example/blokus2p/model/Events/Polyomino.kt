package com.example.blokus2p.model.Events

data class Polyomino(
    val name: String = "",
    val points: Int = 0,
    val isSelected: Boolean = false,
    val selectedCell: Pair<Int,Int> = Pair(0,0),
    val cells: List<Pair<Int,Int>> = listOf()
)
