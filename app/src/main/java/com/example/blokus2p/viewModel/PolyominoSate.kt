package com.example.blokus2p.viewModel

import com.example.blokus2p.game.Polyomino

data class PolyominoSate(
    val playerOnePolyominos : List<Polyomino> = listOf(),
    val playerTwoPolyominos : List<Polyomino> = listOf(),
    val selectedPolyomino: Polyomino? = null
)
