package com.example.blokus2p.viewModel

import androidx.compose.ui.graphics.Color
import com.example.blokus2p.model.Events.Polyomino

data class GameState(
    val activPlayer_id: Int = 0,
    val isFinished: Boolean = false,
    val activPlayer: Player = Player(),
    val playerTwo:Player = Player(),
    val playerOneColor: Color = Color.Black,
    val playerTwoColor: Color = Color.Black,
    val gridSize: Int = 14,
    val boardGrid :Array<Int> = Array(14 * 14) { 0 },
    val selectedPolyomino: Polyomino = Polyomino()
)

data class Player(
    val id: Int = 0,
    val name: String = "",
    val color: Color = Color.Black,
    val points: Int = 0,
    val polyominos: List<Polyomino> =listOf(
        Polyomino(
            "Fünf",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(0, 4))
        ),
        Polyomino(
            "Fünf 7",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2), Pair(2, 1))
        ),
        Polyomino(
            "Fünf L",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(1, 3))
        ),
        Polyomino(
            "Fünf T",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(1, 1))
        ),
        Polyomino(
            "Fünf Z",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2), Pair(2, 2))
        ),
        Polyomino(
            "Fünf Block",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, 2))
        ),
        Polyomino(
            "Fünf Kreuz",
            5,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 1), Pair(2, 1))
        ),
        Polyomino(
            "Fünf Plus",
            5,
            false,
            cells=listOf(Pair(1, 0), Pair(1, 1), Pair(0, 1), Pair(1, 2), Pair(2, 1))
        ),
        Polyomino(
            "Vier",
            4,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3))
        ),
        Polyomino(
            "Vier L",
            4,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2))
        ),
        Polyomino(
            "Vier T",
            4,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 1))
        ),
        Polyomino(
            "Vier Z",
            4,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2))
        ),
        Polyomino(
            "Vier Block",
            4,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1))
        ),
        Polyomino(
            "Drei",
            3,
            false,
            cells=listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2))
        ),
        Polyomino("Drei L", 3, false, cells=listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0))),
        Polyomino("Zwei", 2, false, cells=listOf(Pair(0, 0), Pair(0, 1))),
        Polyomino("Eins", 1, false, cells=listOf(Pair(0, 0))),
    ),
    val placedPolyomino: Polyomino = Polyomino(),
    val edges: List<Int> = listOf()
)