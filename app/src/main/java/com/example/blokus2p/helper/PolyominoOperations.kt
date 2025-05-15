package com.example.blokus2p.helper


import com.example.blokus2p.game.Polyomino

/**
 * mapt die Zellen eines Polyominos auf die Indizes des Boards
 */
fun mapCellsToBoardIndexes(
    polyomino: Polyomino,
    position: Int,
): Polyomino {
    return polyomino.copy(
        cells2 = polyomino.cells2.map { index ->
            index + position
        }
    )
}