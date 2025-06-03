package com.example.blokus2p.model

data class ScoredMove(
    val move: SmalMove?,
    val score: Int,
    val depth: Int = 0
)
