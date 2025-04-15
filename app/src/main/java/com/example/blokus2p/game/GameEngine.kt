package com.example.blokus2p.game

class GameEngine(
    private val board: GameBoard,
    private val rules: GameRules
) {

    fun place(player: Player, polyomino: Polyomino, col: Int, row: Int):GameBoard? {
        if (!rules.isValidPlacement(player, polyomino, board, Pair(col, row))) {
            return null
        }
        val newGrid = board.boardGrid.copyOf()

        val placedCells = polyomino.cells.map { cell ->
            Pair(cell.first + col, cell.second + row)
        }

        placedCells.forEach { (x, y) ->
            newGrid[y * board.boardSize + x] = player.id
        }
        val placedPoly = PlacedPolyomino(
            playerId = player.id,
            cells = placedCells
        )
        return board.copyWith(
            boardGrid = newGrid,
            placedPolyominos = board.placedPolyominos + placedPoly
        )
    }

    fun undoplace():GameBoard?{
        val lastPlacedPolyomino = board.placedPolyominos.lastOrNull()
        if (lastPlacedPolyomino != null) {
            val newGrid = board.boardGrid.copyOf()
            lastPlacedPolyomino.cells.forEach{ ( x, y) ->
                newGrid[y * board.boardSize + x] = 0
            }

            val newBoard = board
            for (cell in lastPlacedPolyomino.cells) {
                newBoard.boardGrid[cell.first+ cell.second  * board.boardSize] = 0
            }
            return board.copyWith(
                boardGrid = newGrid,
                placedPolyominos = board.placedPolyominos - lastPlacedPolyomino
            )
        }else return null
    }
}