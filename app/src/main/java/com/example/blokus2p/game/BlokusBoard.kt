package com.example.blokus2p.game

data class BlokusBoard(
    override val boardSize : Int = 14,
    override val boardGrid : Array<Int> = Array(boardSize * boardSize) { 0 },
    override val placedPolyominos: List<PlacedPolyomino> = listOf(),
 ) : GameBoard{
    override fun copyWith(
        boardGrid: Array<Int>,
        placedPolyominos: List<PlacedPolyomino>
    ): GameBoard = this.copy(
        boardGrid = boardGrid,
        placedPolyominos = placedPolyominos
    )
 }
data class BlokusBoard2(
    val boardSize : Int = 14,
    val boardGrid : LongArray = LongArray(4),
    val placedPolyominos: List<PlacedPolyomino2> = listOf(),
) {
    //    override fun copyWith(
//        boardGrid: Array<Int>,
//        placedPolyominos: List<PlacedPolyomino2>
//    ): GameBoard = this.copy(
//        boardGrid = boardGrid,
//        placedPolyominos = placedPolyominos
//    )

    fun copyWith(boardGrid: LongArray, placedPolyominos: List<PlacedPolyomino2>): BlokusBoard2 =
        this.copy(
            boardGrid = boardGrid,
            placedPolyominos = placedPolyominos
        )

//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as BlokusBoard2
//
//        if (boardSize != other.boardSize) return false
//        if (!boardGrid.contentEquals(other.boardGrid)) return false
//        if (placedPolyominos != other.placedPolyominos) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = boardSize
//        result = 31 * result + boardGrid.contentHashCode()
//        result = 31 * result + placedPolyominos.hashCode()
//        return result
//    }

}