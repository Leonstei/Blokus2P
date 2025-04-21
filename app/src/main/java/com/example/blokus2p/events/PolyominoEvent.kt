package com.example.blokus2p.events

import com.example.blokus2p.game.Polyomino

interface PolyominoEvent : AppEvent {
    data class PolyominoSelected(val polyomino: Polyomino, val selectedCell: Pair<Int,Int>) :
        PolyominoEvent
    object PolyominoRotate: PolyominoEvent
    object PolyominoRotateClockwise: PolyominoEvent
    object PolyominoRotateCounterClockwise: PolyominoEvent
}