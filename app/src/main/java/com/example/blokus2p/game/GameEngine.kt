package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.model.Move

class GameEngine {
    fun place(player: Player, polyomino: Polyomino, col: Int, row: Int, board: GameBoard,rules: GameRules):GameBoard? {
        if (!rules.isValidPlacement(player, polyomino.cells, board, Pair(col, row))) {
            return null
        }
        val newGrid = board.boardGrid.copyOf()

        val placedCells = polyomino.cells.map { cell ->
            Pair(cell.first + col, cell.second + row)
        }

        placedCells.forEach { (x, y) ->
            newGrid[y * board.boardSize + x] = player.id
        }
        val placedPoly = PlacedPolyomino(player.id, polyomino, cells = placedCells)
        return board.copyWith(
            boardGrid = newGrid,
            placedPolyominos = board.placedPolyominos + placedPoly
        )
    }

    fun placeAiMove(player: Player, polyomino: Polyomino, col: Int, row: Int, board: GameBoard,rules: GameRules, orientation: List<Pair<Int,Int>>) : GameBoard?{
        val newBoard =place(player,polyomino.copy(cells = orientation),col,row,board,rules)
        return newBoard
    }

    fun undoplace(board: GameBoard):GameBoard?{
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

    fun calculateNewAvailableEdges(player: Player, board: GameBoard): Set<Int> {
        val newAvailableEdges: MutableSet<Int> = mutableSetOf()
        val lastPlacedPolyominosFromPlayer = board.placedPolyominos.filter { it.playerId == player.id }
        val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()

        if (lastPlacedPolyominoFromPlayer == null) return newAvailableEdges.toSet()

        lastPlacedPolyominoFromPlayer.cells.forEach { (x, y) ->
            val position = y * board.boardSize + x
            val leftTopEdge = position - 15
            val rightTopEdge = position - 13
            val leftBottomEdge = position + 13
            val rightBottomEdge = position + 15
            if (leftTopEdge in 0 until 196 && board.boardGrid[leftTopEdge] == 0
                && position % 14 != 0) newAvailableEdges.add(leftTopEdge)
            if (rightTopEdge in 0 until 196 && board.boardGrid[rightTopEdge] == 0
                && (position-13) % 14 != 0) newAvailableEdges.add(rightTopEdge)
            if (leftBottomEdge in 0 until 196 && board.boardGrid[leftBottomEdge] == 0
                && position % 14 != 0 ) newAvailableEdges.add(leftBottomEdge)
            if (rightBottomEdge in 0 until 196 && board.boardGrid[rightBottomEdge] == 0
                && (position-13) % 14 != 0) newAvailableEdges.add(rightBottomEdge)
        }
        val filteredEdges = newAvailableEdges.filter { edge ->
            val cellAbove = edge - 14
            val cellLeft = edge - 1
            val cellRight = edge + 1
            val cellBelow = edge + 14

            val aboveOk = cellAbove !in 0 until 196 || board.boardGrid[cellAbove] != player.id
            val leftOk = cellLeft !in 0 until 196 || board.boardGrid[cellLeft] != player.id
            val rightOk = cellRight !in 0 until 196 || board.boardGrid[cellRight] != player.id
            val belowOk = cellBelow !in 0 until 196 || board.boardGrid[cellBelow] != player.id

            aboveOk && leftOk && rightOk && belowOk
        }

        return filteredEdges.toSet()
    }
    fun notCheckForNotAvailableEdges(edges: Set<Int>, board: GameBoard):Set<Int>{
        val notAvailableEdges : MutableSet<Int> = mutableSetOf()
        edges.forEach { x->
            if (board.boardGrid[x] != 0) notAvailableEdges.add(x)
        }
        return notAvailableEdges
    }

    fun calculateAllMovesOfAPlayer(player: Player,board: GameBoard,rules: GameRules):List<Move>{
        val validMoves = mutableListOf<Move>()
        val shapes: MutableList<List<Pair<Int,Int>>> = mutableListOf()

        for (polyomino in player.polyominos) {
            // Transformationen: Rotationen & Spiegelungen
            val transformedShapes = polyomino.getAllTransformations()

            for (shape in transformedShapes) {
                for (edge in player.availableEdges) {
                    // Probiere alle Verschiebungen des Polyominos von der Edge aus
                    for (cell in shape) {
                        val newShape = normalizeShapeForCell(cell,shape)
                        shapes.add(newShape)
                        val edgeX = edge%board.boardSize
                        val edgeY:Int = edge/board.boardSize

                        // Ist der Zug erlaubt?
                        if (rules.isValidPlacement(player, newShape, board, Pair(edgeX, edgeY))) {
                            validMoves.add(
                                Move(
                                    polyomino = polyomino,
                                    orientation = newShape,
                                    position = Pair(edgeX, edgeY)
                                )
                            )
                        }
                    }
                }
            }
        }
        Log.d("AppViewModel", "validMoves ${validMoves.size}")
        return validMoves
    }

    private fun normalizeShapeForCell(cell: Pair<Int,Int>, shape: List<Pair<Int,Int>>):List<Pair<Int,Int>>  {
        return shape.map { (x,y)->
            Pair(x-cell.first,y-cell.second)
        }
    }

    fun calculateNewMoves(player: Player){
        val validMoves = mutableListOf<Move>()
        //val newEdges = calculateAllAvailableEdges()
        for (edge in player.availableEdges) {
            for (polyomino in player.polyominos) {
                // Transformationen: Rotationen & Spiegelungen
                val transformedShapes = polyomino.getAllTransformations()

                for (shape in transformedShapes) {

                }
            }
        }
    }
}
