package com.example.blokus2p.model

data class ScoredMove(
    val move: Move?,
    val score: Int,
    val depth: Int = 0
)
