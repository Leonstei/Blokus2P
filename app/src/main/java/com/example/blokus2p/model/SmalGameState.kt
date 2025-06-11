package com.example.blokus2p.model

import com.example.blokus2p.ai.AiInterface
import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.PlacedPolyomino
import com.example.blokus2p.game.Polyomino


data class SmalGameState(
    val players: List<SmalPlayer> = listOf(),
    val activPlayer_id: Int = 0,
    val isFinished: Boolean = false,
    var board : SmalBoard,
){
    fun getResult(perspectivePlayerId: Int): Double {
        val result =
            when {
                players[0].points > players[1].points -> 1.0 // Spieler 1 gewinnt
                players[0].points < players[1].points -> -1.0 // Spieler 2 gewinnt
                else -> 0.0 // Unentschieden
            }
        if(players[0].id == perspectivePlayerId)
            return result
        else
            return -result
    }
}

data class SmalPlayer(
    val id: Int = 0,
    val isActiv: Boolean = false,
    val isMaximizing: Boolean = false,
    val points: Int = 0,
    val bitBoard: LongArray = LongArray(4),
    val polyominos: List<SmalPolyomino> = listOf(
        SmalPolyomino(
            PolyominoNames.FÜNF,
            5,
            cells=listOf(0, 14, 28, 42, 56)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_ZL,
            5,
            cells=listOf(0, 14, 28, 29, 43)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_7,
            5,
            cells=listOf(0, 14, 15, 29, 16)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_L,
            5,
            cells=listOf(0, 14, 28, 42, 43)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_SMAL_T,
            5,
            cells=listOf(0, 14, 28, 42, 15)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_W,
            5,
            cells=listOf(0, 14, 15, 29, 30)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_Z,
            5,
            cells=listOf(0, 14, 15, 16, 30)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_LANG_L,
            5,
            cells=listOf(0, 14, 28, 29, 30)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_C,
            5,
            cells=listOf(0, 14, 1, 2, 16)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_BLOCK,
            5,
            cells=listOf(0, 14, 1, 15, 29)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_T,
            5,
            cells=listOf(0, 1, 2, 15, 29)
        ),
        SmalPolyomino(
            PolyominoNames.FÜNF_PLUS,
            5,
            cells=listOf(1, 14, 15, 29, 16)
        ),
        SmalPolyomino(
            PolyominoNames.VIER,
            4,
            cells=listOf(0, 14, 28, 42)
        ),
        SmalPolyomino(
            PolyominoNames.VIER_L,
            4,
            cells=listOf(0, 14, 28, 29)
        ),
        SmalPolyomino(
            PolyominoNames.VIER_T,
            4,
            cells=listOf(0, 14, 28, 15)
        ),
        SmalPolyomino(
            PolyominoNames.VIER_Z,
            4,
            cells=listOf(0, 14, 15, 29)
        ),
        SmalPolyomino(
            PolyominoNames.VIER_BLOCK,
            4,
            cells=listOf(0, 14, 1, 15)
        ),
        SmalPolyomino(
            PolyominoNames.DREI,
            3,
            cells=listOf(0, 14, 28)
        ),
        SmalPolyomino(PolyominoNames.DREI_L, 3,  cells=listOf(0, 14, 1)),
        SmalPolyomino(PolyominoNames.ZWEI, 2,  cells=listOf(0, 14)),
        SmalPolyomino(PolyominoNames.EINS, 1,  cells=listOf(0)),
    ),
    //val placedSmalPolyomino: SmalPolyomino = SmalPolyomino(),
    val availableEdges: Set<Int> = setOf(),
    val availableMoves: Set<SmalMove> = setOf(),
    val isAi: Boolean = false,
    val ai: AiInterface? = null,
)

data class SmalPolyomino(
    val name: PolyominoNames =PolyominoNames.NULL,
    val points: Int = 0,
    val cells: List<Int> = listOf(),
    ){
    val distinctVariants: List<List<Int>> = polyominoVariantsDistinct[name] ?: emptyList()
}

data class SmalMove(
    val polyomino: SmalPolyomino,
//    val orientation: List<Int>, // eine aus allVariants
    val position: Int // Startposition
)
data class SmalBoard(
    val boardGrid: LongArray = LongArray(4),
    val size: Int = 14,
    val placedPolyominosSmal: List<PlacedSmalPolyomino> = listOf()
) {
    fun copyWith(boardGrid: LongArray, placedPolyominos: List<PlacedSmalPolyomino>): SmalBoard =
        this.copy(
            boardGrid = boardGrid,
            placedPolyominosSmal = placedPolyominos
        )
}

data class PlacedSmalPolyomino(
    val playerId: Int,
    val polyomino: SmalPolyomino,
    val cells: List<Int>,
    val placePosition: Int
)


