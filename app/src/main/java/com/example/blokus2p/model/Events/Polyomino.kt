package com.example.blokus2p.model.Events

data class Polyomino(
    val name: String = "",
    val points: Int = 0,
    val isSelected: Boolean = false,
    val cells: List<Pair<Int, Int>> = listOf(),
    val selectedCell: Pair<Int, Int> = Pair(0, 0),  // Zelle, die ausgewählt ist
    val variantIndex: Int = 0  // Welche Variante ist aktiv
) {
    val allVariants: List<PolyominoVariant> by lazy {
        generateAllTransformations(cells)
    }

    val currentVariant: List<Pair<Int, Int>>
        get() = allVariants[variantIndex % allVariants.size].cells

    fun rotatedRight(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation + 90) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        return copy(variantIndex = allVariants.indexOf(next))
    }
    fun rotatedLeft(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == (current.rotation - 90 + 360) % 360 && it.isFlipped == current.isFlipped
        } ?: current
        return copy(variantIndex = allVariants.indexOf(next))
    }

    fun flippHorizontal(): Polyomino {
        val current = allVariants[variantIndex]
        val next = allVariants.firstOrNull {
            it.rotation == current.rotation  && it.isFlipped != current.isFlipped
        } ?: current
        return copy(variantIndex = allVariants.indexOf(next))
    }

    private fun generateAllTransformations(cells: List<Pair<Int, Int>>): List<PolyominoVariant> {
        val variants = mutableSetOf<List<Pair<Int, Int>>>()
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
            polyominoVariants.add(PolyominoVariant(
                cells = normalize(flipHorizontal(current)),
                isFlipped = true,
                rotation = rotation
            ))
            current = rotate90(current)
            rotation += 90
        }

        return polyominoVariants.toList()
    }

    private fun rotate90(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
        cells.map { Pair(-it.second, it.first) }

    private fun flipHorizontal(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
        cells.map { Pair(-it.first, it.second) }

    private fun normalize(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }
        return cells.map { Pair(it.first - minX, it.second - minY) }.sortedBy { it.first * 10 + it.second }
    }
}

data class PolyominoVariant(
    val cells: List<Pair<Int, Int>>,
    val isFlipped: Boolean,
    val rotation: Int
)
