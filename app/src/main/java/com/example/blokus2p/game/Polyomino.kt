package com.example.blokus2p.game


data class Polyomino(
    val name: String = "",
    val points: Int = 0,
    val isSelected: Boolean = false,
    val cells: List<Int> = listOf(),
    val selectedCell: Int = 0,
    val variantIndex: Int = 0,
    val boardSize: Int = 14
) {
    private val allVariants: List<PolyominoVariant> by lazy {
        generateAllTransformations(cells)
    }
    private val cachedTransformations: List<List<Int>> by lazy {
        allVariants.map { it.cells }.distinct()
    }
    fun getAllTransformations():List<List<Int>> = cachedTransformations
//    {
//        val allTransformations: MutableSet<List<Int>> = mutableSetOf()
//        allVariants.forEach {  polyominoVariant ->
//            allTransformations.add(polyominoVariant.cells)
//        }
//        return allTransformations.toList()
//    }

    val currentVariant: List<Int>
        get() = allVariants[variantIndex % allVariants.size].cells

    fun rotatedRight(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation + 90) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf( selectedCell)
        val currentRotated = rotate90(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated, currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = pairToIndex(selectedCellNormalized))
    }
    fun rotatedLeft(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation - 90 + 360) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell)
        val currentRotated = rotate90Left(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated,currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = pairToIndex(selectedCellNormalized))
    }

    fun flippHorizontal(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == current.rotation  && it.isFlipped != current.isFlipped
        } ?: current
        val selectedCellIndex = currentVariant.indexOf(selectedCell)
        val currentRotated = flipHorizontal(toPairs(currentVariant))
        val selectedCellNormalized = normalizeOneCell(currentRotated, currentRotated[selectedCellIndex])

        return copy(variantIndex = allVariants.indexOf(next), selectedCell = pairToIndex(selectedCellNormalized))
    }

    fun generateAllTransformations(
        cellIndices: List<Int>,
    ): List<PolyominoVariant> {
        val seen = mutableSetOf<PolyominoVariant>()


        var rotation = 0
        var currentCells = toPairs(cellIndices)

        repeat(4) {
            // normalize() und flipHorizontal(), rotate90() arbeiten auf Pair-Listen
            val norm       = normalize(currentCells)
            val normFlip   = normalize(flipHorizontal(currentCells))

            seen += PolyominoVariant(
                cells      = toIndices(norm),
                isFlipped  = false,
                rotation   = rotation
            )
            seen += PolyominoVariant(
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

}
data class PolyominoVariant(
    val cells: List<Int>,
    val isFlipped: Boolean,
    val rotation: Int
)

data class PlacedPolyomino(
    val playerId: Int,
    val polyomino: Polyomino,
    val cells: List<Int>,
    val placePosition: Int
)
