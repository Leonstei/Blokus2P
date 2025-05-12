package com.example.blokus2p.game

import android.util.Log



data class Polyomino(
    val name: String = "",
    val points: Int = 0,
    val isSelected: Boolean = false,
    val cells: List<Pair<Int, Int>> = listOf(),
    val cells2: List<Int> = listOf(),
    val selectedCell2: Int = 0,
    val selectedCell: Pair<Int, Int> = Pair(0, 0),
    val variantIndex: Int = 0,
    val boardSize: Int = 14
) {
    private val allVariants: List<PolyominoVariant2> by lazy {
        generateAllTransformations(cells2)
    }
    fun getAllTransformations():List<List<Int>> {
        val allTransformations: MutableSet<List<Int>> = mutableSetOf()
        allVariants.forEach {  polyominoVariant ->
            allTransformations.add(polyominoVariant.cells)
        }
        return allTransformations.toList()
    }

    val currentVariant: List<Int>
        get() = allVariants[variantIndex % allVariants.size].cells

    fun rotatedRight(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation + 90) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf( selectedCell2)
        val currentRotated = rotate90(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated, currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized, selectedCell2 = pairToIndex(selectedCellNormalized))
    }
    fun rotatedLeft(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation - 90 + 360) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell2)
        val currentRotated = rotate90Left(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated,currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized,selectedCell2 = pairToIndex(selectedCellNormalized))
    }

    fun flippHorizontal(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == current.rotation  && it.isFlipped != current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell2)
        val currentRotated = flipHorizontal(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated, currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized,selectedCell2 = pairToIndex(selectedCellNormalized))
    }

    fun generateAllTransformations(
        cellIndices: List<Int>,
    ): List<PolyominoVariant2> {
        val seen = mutableSetOf<PolyominoVariant2>()


        var rotation = 0
        var currentCells = toPairs(cellIndices)

        repeat(4) {
            // normalize() und flipHorizontal(), rotate90() arbeiten auf Pair-Listen
            val norm       = normalize(currentCells)
            val normFlip   = normalize(flipHorizontal(currentCells))

            seen += PolyominoVariant2(
                cells      = toIndices(norm),
                isFlipped  = false,
                rotation   = rotation
            )
            seen += PolyominoVariant2(
                cells      = toIndices(normFlip),
                isFlipped  = true,
                rotation   = rotation
            )

            // für nächste Runde
            currentCells = rotate90(currentCells)
            rotation += 90
        }

        return seen.toList()
    }


    private fun rotate90(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
        cells.map { Pair(-it.second, it.first) }
    private fun rotate90Left(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
        cells.map { Pair(it.second, -it.first) }

    private fun flipHorizontal(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
        cells.map { Pair(-it.first, it.second) }

    private fun normalize(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }
        return cells.map { Pair(it.first - minX, it.second - minY) }.sortedWith(compareBy({ it.second }, { it.first }))
    }
    private fun normalizeOneCell(cells: List<Pair<Int, Int>>,selectedCell: Pair<Int, Int>): Pair<Int, Int> {
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }
        return  Pair(selectedCell.first - minX, selectedCell.second - minY)
    }
    fun toPairs(indices: List<Int>) = indices.map {
        val x = it % boardSize
        val y = it / boardSize
        x to y
    }
    fun toIndices(pairs: List<Pair<Int,Int>>) = pairs.map { (x, y) ->
        y * boardSize + x
    }
    fun pairToIndex(pair: Pair<Int,Int>): Int = pair.second * boardSize + pair.first
    fun indexToPair(index: Int, width: Int): Pair<Int, Int> = Pair(index % width, index / width)

}
data class PolyominoVariant(
    val cells: List<Pair<Int, Int>>,
    val isFlipped: Boolean,
    val rotation: Int
)
data class PolyominoVariant2(
    val cells: List<Int>,
    val isFlipped: Boolean,
    val rotation: Int
)

data class PlacedPolyomino(
    val playerId: Int,
    val polyomino: Polyomino,
    val cells: List<Pair<Int, Int>>,
    val placePosition: Pair<Int, Int>
)
data class PlacedPolyomino2(
    val playerId: Int,
    val polyomino: Polyomino,
    val cells: List<Int>,
    val placePosition: Int
)
