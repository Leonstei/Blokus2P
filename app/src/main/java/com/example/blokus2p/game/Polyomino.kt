package com.example.blokus2p.game

import android.util.Log


data class PolyominoVariant(
    val cells: List<Pair<Int, Int>>,
    val isFlipped: Boolean,
    val rotation: Int
)

data class Polyomino(
    val name: String = "",
    val points: Int = 0,
    val isSelected: Boolean = false,
    val cells: List<Pair<Int, Int>> = listOf(),
    val cells2: List<Int> = listOf(),
    val selectedCell2: Int = 0,
    val selectedCell: Pair<Int, Int> = Pair(0, 0),
    val variantIndex: Int = 0
) {
    private val allVariants: List<PolyominoVariant> by lazy {
        generateAllTransformations(cells)
    }
    fun getAllTransformations():List<List<Pair<Int,Int>>> {
        val allTransformations: MutableSet<List<Pair<Int,Int>>> = mutableSetOf()
        allVariants.forEach {  polyominoVariant ->
            allTransformations.add(polyominoVariant.cells)
        }
        return allTransformations.toList()
    }

    val currentVariant: List<Pair<Int, Int>>
        get() = allVariants[variantIndex % allVariants.size].cells

    fun rotatedRight(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation + 90) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell)
        val currentRotated = rotate90(currentVariant)
        val selectedCellNormalized = normalizeOneCell(currentRotated,currentRotated.get(selectedCellIndex))

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized)
    }
    fun rotatedLeft(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation - 90 + 360) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell)
        val currentRotated = rotate90Left(currentVariant)
        val selectedCellNormalized = normalizeOneCell(currentRotated,currentRotated.get(selectedCellIndex))

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized)
    }

    fun flippHorizontal(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == current.rotation  && it.isFlipped != current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell)
        val currentRotated = flipHorizontal(currentVariant)
        val selectedCellNormalized = normalizeOneCell(currentRotated,currentRotated.get(selectedCellIndex))

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = selectedCellNormalized)
    }

    private fun generateAllTransformations(cells: List<Pair<Int, Int>>): List<PolyominoVariant> {
        val polyominoVariants = mutableSetOf<PolyominoVariant>()
        var rotation = 0

        var current = cells
        repeat(4) {
            polyominoVariants.add(
                PolyominoVariant(
                    cells = normalize(current),
                    isFlipped = false,
                    rotation = rotation
                )
            )
            polyominoVariants.add(
                PolyominoVariant(
                    cells = normalize(flipHorizontal(current)),
                    isFlipped = true,
                    rotation = rotation
                )
            )
            current = rotate90(current)
            rotation += 90
        }

        return polyominoVariants.toList()
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
}
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
