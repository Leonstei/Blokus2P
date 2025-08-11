package com.example.blokus2p.model


data class Move(
    val polyomino: Polyomino,
    val orientation: List<Int>, // eine aus allVariants
    val position: Int // Startposition
)