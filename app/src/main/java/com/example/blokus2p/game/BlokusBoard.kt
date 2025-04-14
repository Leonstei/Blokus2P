package com.example.blokus2p.game

data class BlokusBoard(
    override val boardSize : Int = 14,
    override val boardGrid : Array<Int> = Array(boardSize * boardSize) { 0 },
    override var placedPolyominos: Map<Int,List<Pair<Int,Int>>> = mapOf(),
 ) : GameBoard{
     override fun putPlacedPolyomino(playerId: Int, cells: List<Pair<Int,Int>>): BlokusBoard {
         val newPlacedPolyominos = placedPolyominos.toMutableMap()
         newPlacedPolyominos[playerId] = cells
         return this.copy(placedPolyominos = newPlacedPolyominos)
     }
 }