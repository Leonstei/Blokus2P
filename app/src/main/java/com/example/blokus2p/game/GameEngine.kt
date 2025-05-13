package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.Move2
import kotlin.time.measureTime

class GameEngine {
    fun place(
        player: Player,
        polyomino: Polyomino,
        col: Int,
        row: Int,
        board: GameBoard,
        rules: GameRules
    ): GameBoard? {
        if (!rules.isValidPlacement(player, polyomino.cells, board, Pair(col, row))) {
            return null
        }

        val boardSize = board.boardSize
        val newGrid = board.boardGrid.copyOf()
        val newBitBoard = player.bitBoard.copyOf()

        val placedCells = polyomino.cells.map { cell ->
            Pair(cell.first + col, cell.second + row)
        }

        val placedCellsIndexed = placedCells.map { (x, y) ->
            y * boardSize + x
        }
        val timeTaken = measureTime {
            placedCellsIndexed.forEach {
                val wordIndex = it / 64
                val bitIndex = it % 64
                newBitBoard[wordIndex] = newBitBoard[wordIndex] or (1L shl bitIndex)
            }
        }
        Log.d("AppViewModel", "Time taken change LongArray: $timeTaken ")

        val timemesure = measureTime {
            placedCells.forEach { (x, y) ->
                newGrid[y * board.boardSize + x] = player.id
            }
        }
        Log.d("AppViewModel", "Time taken change intArray: $timemesure ")


        val placedPoly = PlacedPolyomino(player.id, polyomino, placedCells, Pair(col, row))

        return board.copyWith(
            boardGrid = newGrid,
            placedPolyominos = board.placedPolyominos + placedPoly,
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

        if (lastPlacedPolyominoFromPlayer == null) return setOf(65,130)

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

    fun calculateAllMovesOfAPlayer(player: Player, board: BlokusBoard2, rules: GameRules):Set<Move2>{
        val validMoves = mutableListOf<Move2>()
            for (polyomino in player.newPolyominos) {
                // Transformationen: Rotationen & Spiegelungen
                val transformedShapes = polyomino.getAllTransformations()
                for (shape in transformedShapes) {
                    for (edge in player.availableEdges) {
                        // Probiere alle Verschiebungen des Polyominos von der Edge aus
                        for (cell in shape) {
                            val newShape = normalizeShapeForCell(cell, shape)
                            val boardPositions = newShape.map { cell ->
                                cell + edge
                            }
                            // Ist der Zug erlaubt?
                            if (rules.isValidPlacement(
                                    player,
                                    boardPositions,
                                    board,
                                    edge
                                )
                            ) {
                                validMoves.add(
                                    Move2(
                                        polyomino = polyomino,
                                        orientation = boardPositions,
                                        position = edge
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Log.d("AppViewModel", "validMoves ${validMoves.size}")

        return validMoves.toSet()
    }

    private fun normalizeShapeForCell(cell: Int, shape: List<Int>): List<Int> {
        return shape.map { index ->
            index - cell
        }
    }

    fun calculateNotAvailableMoves2(player: Player, validMoves: List<Move>,board: GameBoard,rules: GameRules): Set<Move> {
        val notAvailableMoves = mutableSetOf<Move>()
        validMoves.forEach { move->
            if(!rules.isValidPlacement(player,move.orientation,board,move.position)){
                notAvailableMoves.add(move)
            }
            if (move.polyomino.name == board.placedPolyominos.last().polyomino.name){
                notAvailableMoves.add(move)
            }
        }
        Log.d("AppViewModel", "not validMoves fun2 ${notAvailableMoves.size}")
        return notAvailableMoves
    }
    fun calculateNotAvailableMoves(player: Player, board: GameBoard):List<Move2>{

        val lastPlacedPolyominosFromPlayer =
            board.placedPolyominos.filter { it.playerId == player.id }
        val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()
        if (lastPlacedPolyominoFromPlayer == null) return emptyList()

        //funktioniert nicht bei mehr als 2 Spielern
        val lastPlacedPolyominosFromOtherPlayer =
            board.placedPolyominos.filter { it.playerId != player.id }
        val lastPlacedPolyominoFromOtherPlayer =
            lastPlacedPolyominosFromOtherPlayer.lastOrNull()

        val notAvailableEdges: MutableList<Pair<Int, Int>> =
            mutableListOf(lastPlacedPolyominoFromPlayer.placePosition)

        if (lastPlacedPolyominoFromOtherPlayer != null) {
            lastPlacedPolyominoFromOtherPlayer.cells.forEach {
                notAvailableEdges.add(it)
            }
        }
        lastPlacedPolyominoFromPlayer.cells.forEach {
            notAvailableEdges.add(Pair(it.first+1,it.second))
            notAvailableEdges.add(Pair(it.first-1,it.second))
            notAvailableEdges.add(Pair(it.first,it.second+1))
            notAvailableEdges.add(Pair(it.first,it.second-1))
        }

        val notValidMoves  = player.availableMoves.filter { move ->
            notAvailableEdges.contains(indexToPair(move.position,14))
                    //||
//                    move.orientation.any { cell ->
//                        cell.copy(
//                            first = cell.first + move.position.first,
//                            second = cell.second + move.position.second
//                        ) in notAvailableEdges
//                    }
                    || move.polyomino.name == lastPlacedPolyominoFromPlayer.polyomino.name

        }

        //Log.d("AppViewModel", "not validMoves fun1 ${notValidMoves.size}")
        return notValidMoves
    }
    fun pairToIndex(pair: Pair<Int,Int>,boardSize: Int): Int = pair.second * boardSize + pair.first
    fun indexToPair(index: Int, width: Int): Pair<Int, Int> = Pair(index % width, index / width)


    fun calculateNewMoves(player: Player, board: GameBoard, rules: GameRules): List<Move> {
        val validMoves = mutableListOf<Move>()
        val newEdges = calculateNewAvailableEdges(player, board)

        val lastPlacedPolyominosFromPlayer = board.placedPolyominos.filter { it.playerId == player.id }
        val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()

        if (lastPlacedPolyominoFromPlayer == null) return emptyList()

        for (polyomino in player.polyominos) {
            // Transformationen: Rotationen & Spiegelungen
            val transformedShapes = polyomino.getAllTransformations()
            for (shape in transformedShapes) {
                for (edge in newEdges) {
                    // Probiere alle Verschiebungen des Polyominos von der Edge aus
                    for (cell in shape) {
                        val newShape = normalizeShapeForCell(cell, shape)
                        val edgeX = edge % board.boardSize
                        val edgeY: Int = edge / board.boardSize

                        // Ist der Zug erlaubt?
                        if (rules.isValidPlacement(player, listOf(Pair(edgeX,edgeY)), board, Pair(edgeX, edgeY))) {
                            validMoves.add(
                                Move(
                                    polyomino = polyomino,
                                    orientation = listOf(Pair(edgeX,edgeY)),
                                    position = Pair(edgeX, edgeY)
                                )
                            )
                        }
                    }
                }
            }
        }
//        Log.d("AppViewModel", "new validMoves ${validMoves.size}")
        return validMoves
    }

    fun checkForGameEnd(players :List<Player>): Boolean {
        var countPlayersFinished= 0
        for (player in players) {
            if (player.polyominos.isEmpty() || player.availableEdges.isEmpty() || player.availableMoves.isEmpty()) {
                countPlayersFinished ++
            }
        }
        return countPlayersFinished == players.size
    }
}
