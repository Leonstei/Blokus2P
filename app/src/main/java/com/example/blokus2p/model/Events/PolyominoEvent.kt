package com.example.blokus2p.model.Events

interface PolyominoEvent : AppEvent {
    data class PolyominoSelected(val polyomino: Polyomino) : PolyominoEvent
    object PolyominoRotate: PolyominoEvent
    object PolyominoRotateClockwise: PolyominoEvent
}