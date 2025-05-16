package com.example.blokus2p.game

data class BlokusBoard(
    override val boardSize : Int = 14,
    override val boardGrid : LongArray = LongArray(4),
    override val placedPolyominos: List<PlacedPolyomino> = listOf(),
):GameBoard {
    override fun copyWith(boardGrid: LongArray, placedPolyominos: List<PlacedPolyomino>): BlokusBoard =
        this.copy(
            boardGrid = boardGrid,
            placedPolyominos = placedPolyominos
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlokusBoard

        if (boardSize != other.boardSize) return false
        if (!boardGrid.contentEquals(other.boardGrid)) return false
        if (placedPolyominos != other.placedPolyominos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = boardSize
        result = 31 * result + boardGrid.contentHashCode()
        result = 31 * result + placedPolyominos.hashCode()
        return result
    }
}