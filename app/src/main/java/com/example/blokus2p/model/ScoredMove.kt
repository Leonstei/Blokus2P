package com.example.blokus2p.model

data class ScoredMove(
    val move: Move?,
    val score: Int,
    val depth: Int = 0
)
data class ScoredMove2(
    val move: Move2?,
    val score: Int,
    val depth: Int = 0
)
