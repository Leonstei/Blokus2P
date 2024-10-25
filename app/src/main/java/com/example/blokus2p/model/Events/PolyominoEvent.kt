package com.example.blokus2p.model.Events

interface PolyominoEvent : AppEvent {
    data class PolyominoSelected(val polyomino: Polyomino,val selectedCell: Pair<Int,Int>) : PolyominoEvent
    object PolyominoRotate: PolyominoEvent
    object PolyominoRotateClockwise: PolyominoEvent
    object PolyominoRotateCounterClockwise: PolyominoEvent
}