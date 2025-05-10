package com.example.blokus2p.model

import com.example.blokus2p.game.Polyomino

data class Move(
    val polyomino: Polyomino,
    val orientation: List<Pair<Int, Int>>, // eine aus allVariants
    val position: Pair<Int, Int> // Startposition
)

data class Move2(
    val polyomino: Polyomino,
    val orientation: List<Int>, // eine aus allVariants
    val position: Int // Startposition
)