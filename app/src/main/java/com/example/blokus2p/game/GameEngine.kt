package com.example.blokus2p.game

class GameEngine(
    private val board: GameBoard,
    private val rules: GameRules
) {

    fun place(player: Player, polyomino: Polyomino, col: Int, row: Int):GameBoard? {
        if(rules.isValidPlacement(player, polyomino, board, Pair(col, row))) {
            val newBoard = board
            val placedPolyomino: MutableList<Pair<Int,Int>> = mutableListOf()
            for (cell in polyomino.cells) {
                val xPosition = cell.first + col
                val yPosition = cell.second + row
                newBoard.boardGrid[xPosition + yPosition * board.boardSize] = player.id
                placedPolyomino.add(Pair(xPosition, yPosition))
            }
            newBoard.putPlacedPolyomino(player.id, placedPolyomino) //placedPolyomino wird nicht richtig abgespeichert
            return newBoard
        }
        else return null
    }

    fun undoplace():GameBoard?{
        val lastPlacedPolyomino = board.placedPolyominos.values.lastOrNull()
        if (lastPlacedPolyomino != null) {
            val newBoard = board
            for (cell in lastPlacedPolyomino) {
                newBoard.boardGrid[cell.first+ cell.second  * board.boardSize] = 0
            }
            return newBoard
        }else return null
    }
}