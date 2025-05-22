package com.example.blokus2p.game

import androidx.compose.ui.graphics.Color
import com.example.blokus2p.ai.AiInterface
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.PolyominoNames

data class Player(
    val id: Int = 0,
    val name: String = "",
    val isActiv: Boolean = false,
    val isMaximizing: Boolean = false,
    val color: Color = Color.Black,
    val points: Int = 0,
    val bitBoard: LongArray = LongArray(4),
    val polyominos: List<Polyomino> = listOf(
        Polyomino(
            PolyominoNames.FÜNF,
            5,
            false,
            cells=listOf(0, 14, 28, 42, 56)
        ),
        Polyomino(
            PolyominoNames.FÜNF_ZL,
            5,
            false,
            cells=listOf(0, 14, 28, 29, 43)
        ),
        Polyomino(
            PolyominoNames.FÜNF_7,
            5,
            false,
            cells=listOf(0, 14, 15, 29, 16)
        ),
        Polyomino(
            PolyominoNames.FÜNF_L,
            5,
            false,
            cells=listOf(0, 14, 28, 42, 43)
        ),
        Polyomino(
            PolyominoNames.FÜNF_SMAL_T,
            5,
            false,
            cells=listOf(0, 14, 28, 42, 15)
        ),
        Polyomino(
            PolyominoNames.FÜNF_W,
            5,
            false,
            cells=listOf(0, 14, 15, 29, 30)
        ),
        Polyomino(
            PolyominoNames.FÜNF_Z,
            5,
            false,
            cells=listOf(0, 14, 15, 16, 30)
        ),
        Polyomino(
            PolyominoNames.FÜNF_LANG_L,
            5,
            false,
            cells=listOf(0, 14, 28, 29, 30)
        ),
        Polyomino(
            PolyominoNames.FÜNF_C,
            5,
            false,
            cells=listOf(0, 14, 1, 2, 16)
        ),
        Polyomino(
            PolyominoNames.FÜNF_BLOCK,
            5,
            false,
            cells=listOf(0, 14, 1, 15, 29)
        ),
        Polyomino(
            PolyominoNames.FÜNF_T,
            5,
            false,
            cells=listOf(0, 1, 2, 15, 29)
        ),
        Polyomino(
            PolyominoNames.FÜNF_PLUS,
            5,
            false,
            cells=listOf(1, 14, 15, 29, 16)
        ),
        Polyomino(
            PolyominoNames.VIER,
            4,
            false,
            cells=listOf(0, 14, 28, 42)
        ),
        Polyomino(
            PolyominoNames.VIER_L,
            4,
            false,
            cells=listOf(0, 14, 28, 29)
        ),
        Polyomino(
            PolyominoNames.VIER_T,
            4,
            false,
            cells=listOf(0, 14, 28, 15)
        ),
        Polyomino(
            PolyominoNames.VIER_Z,
            4,
            false,
            cells=listOf(0, 14, 15, 29)
        ),
        Polyomino(
            PolyominoNames.VIER_BLOCK,
            4,
            false,
            cells=listOf(0, 14, 1, 15)
        ),
        Polyomino(
            PolyominoNames.DREI,
            3,
            false,
            cells=listOf(0, 14, 28)
        ),
        Polyomino(PolyominoNames.DREI_L, 3, false, cells=listOf(0, 14, 1)),
        Polyomino(PolyominoNames.ZWEI, 2, false, cells=listOf(0, 14)),
        Polyomino(PolyominoNames.EINS, 1, false, cells=listOf(0)),
    ),
    val placedPolyomino: Polyomino = Polyomino(),
    val polyominoIsPlaced: Boolean = false,
    val availableEdges: Set<Int> = setOf(),
    val availableMoves: Set<Move> = setOf(),
    val isAi: Boolean = false,
    val ai: AiInterface? = null,
)