package com.example.blokus2p.game

import com.example.blokus2p.helper.setBit
import com.example.blokus2p.model.PlacedPolyomino

data class BlokusBoard(
    override val boardSize : Int = 16,
    override val boardGrid : LongArray = run {
        val grid = LongArray(4)
        // Setze hier die gewünschten Bits
        val indices = mutableListOf<Int>()
        for( i in 0 until 16) {
            setBit(grid, i) // Setze die ersten 16 Bits für die erste Zeile
            setBit(grid, i + 240) // Setze die letzten 16 Bits für die letzte Zeile
        }
        for (i in 1 until 15) {
            setBit(grid, i * 16) // Setze die ersten 16 Bits für die erste Spalte
            setBit(grid, i * 16 + 15) // Setze die letzten 16 Bits für die letzte Spalte
        }
        grid
    },
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