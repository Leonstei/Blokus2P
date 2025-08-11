package com.example.blokus2p.model

import androidx.compose.ui.graphics.Color
import com.example.blokus2p.ai.AiInterface
import com.example.blokus2p.helper.PolyominoNames

data class Player(
    val id: Int = 0,
    val name: String = "",
    val isActiv: Boolean = false,
    val color: Color = Color.Black,
    val points: Int = 0,
    val bitBoard: LongArray = LongArray(4),
    val polyominos: List<Polyomino> = listOf(
        Polyomino(
            PolyominoNames.FÜNF,
            5,
            false,
            cells=listOf(17, 33, 49, 65, 81)
        ),
        Polyomino(
            PolyominoNames.FÜNF_ZL,
            5,
            false,
            cells=listOf(17, 33, 34, 50, 66)
        ),
        Polyomino(
            PolyominoNames.FÜNF_7,
            5,
            false,
            cells=listOf(17, 33, 34, 35, 50)
        ),
        Polyomino(
            PolyominoNames.FÜNF_L,
            5,
            false,
            cells=listOf(17, 33, 49, 65, 66)
        ),
        Polyomino(
            PolyominoNames.FÜNF_SMAL_T,
            5,
            false,
            cells=listOf(17, 33, 34, 49, 65)
        ),
        Polyomino(
            PolyominoNames.FÜNF_W,
            5,
            false,
            cells=listOf(17, 33, 34, 50, 51)
        ),
        Polyomino(
            PolyominoNames.FÜNF_Z,
            5,
            false,
            cells=listOf(17, 33, 34, 35, 51)
        ),
        Polyomino(
            PolyominoNames.FÜNF_LANG_L,
            5,
            false,
            cells=listOf(17, 33, 49, 50, 51)
        ),
        Polyomino(
            PolyominoNames.FÜNF_C,
            5,
            false,
            cells=listOf(17, 33, 18, 19, 35)
        ),
        Polyomino(
            PolyominoNames.FÜNF_BLOCK,
            5,
            false,
            cells=listOf(17, 18, 33, 34, 50)
        ),
        Polyomino(
            PolyominoNames.FÜNF_T,
            5,
            false,
            cells=listOf(17, 18, 19, 34, 50)
        ),
        Polyomino(
            PolyominoNames.FÜNF_PLUS,
            5,
            false,
            cells=listOf(18, 33, 34, 50, 35)
        ),
        Polyomino(
            PolyominoNames.VIER,
            4,
            false,
            cells=listOf(17, 33, 49, 65)
        ),
        Polyomino(
            PolyominoNames.VIER_L,
            4,
            false,
            cells=listOf(17, 33, 49, 50)
        ),
        Polyomino(
            PolyominoNames.VIER_T,
            4,
            false,
            cells=listOf(17, 33, 49, 34)
        ),
        Polyomino(
            PolyominoNames.VIER_Z,
            4,
            false,
            cells=listOf(17, 33, 34, 50)
        ),
        Polyomino(
            PolyominoNames.VIER_BLOCK,
            4,
            false,
            cells=listOf(17, 18, 33, 34)
        ),
        Polyomino(
            PolyominoNames.DREI,
            3,
            false,
            cells=listOf(17, 33, 49)
        ),
        Polyomino(PolyominoNames.DREI_L, 3, false, cells=listOf(17, 18, 33)),
        Polyomino(PolyominoNames.ZWEI, 2, false, cells=listOf(17, 33)),
        Polyomino(PolyominoNames.EINS, 1, false, cells=listOf(17)),
    ),
    val placedPolyomino: Polyomino = Polyomino(),
    val polyominoIsPlaced: Boolean = false,
    val availableEdges: Set<Int> = setOf(),
    val availableMoves: Set<Move> = setOf(),
    val isAi: Boolean = false,
    val ai: AiInterface? = null,
)