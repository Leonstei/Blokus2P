package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.helper.clearBit
import com.example.blokus2p.helper.isBitSet
import com.example.blokus2p.helper.setBit
import com.example.blokus2p.model.Move
import kotlin.time.measureTime

class GameEngine {
    fun place(
        player: Player,
        polyomino: Polyomino,
        position: Int,
        board: BlokusBoard,
        rules: GameRules
    ): BlokusBoard? {
        if (!rules.isValidPlacement(player, polyomino.cells, board, position)) {
            return null
        }
        val newGrid = board.boardGrid.copyOf()
        polyomino.cells.forEach {
            setBit(newGrid, it)
        }
        val placedPoly = PlacedPolyomino(player.id, polyomino, polyomino.cells, position)
        return board.copyWith(
            boardGrid = newGrid,
            placedPolyominos = board.placedPolyominos + placedPoly,
        )
    }

    fun placeAiMove(player: Player, polyomino: Polyomino, position:Int, board: BlokusBoard, rules: GameRules, orientation: List<Int>) : BlokusBoard?{
        val newBoard =place(player,polyomino.copy(cells = orientation),position,board,rules)
        return newBoard
    }

    fun undoplace(board: BlokusBoard):BlokusBoard?{
        val lastPlacedPolyomino = board.placedPolyominos.lastOrNull()
        if (lastPlacedPolyomino != null) {
            val newGrid = board.boardGrid.copyOf()

            lastPlacedPolyomino.cells.forEach{ index ->
                clearBit(newGrid, index)
            }

            return board.copyWith(
                boardGrid = newGrid,
                placedPolyominos = board.placedPolyominos - lastPlacedPolyomino
            )
        } else return null
    }

    fun calculateNewAvailableEdges(player: Player, board: BlokusBoard): Set<Int> {
        val newAvailableEdges: MutableSet<Int> = mutableSetOf()
        val lastPlacedPolyominoFromPlayer = board.placedPolyominos.lastOrNull { it.playerId == player.id }
        //val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()

        if (lastPlacedPolyominoFromPlayer == null) return setOf(65,130)

        lastPlacedPolyominoFromPlayer.cells.forEach { index ->
            val leftTopEdge = index - 15
            val rightTopEdge = index - 13
            val leftBottomEdge = index + 13
            val rightBottomEdge = index + 15
            if (leftTopEdge in 0 until 196 && !isBitSet(board.boardGrid,leftTopEdge)
                && index % 14 != 0) newAvailableEdges.add(leftTopEdge)
            if (rightTopEdge in 0 until 196 && !isBitSet(board.boardGrid,rightTopEdge)
                && (index-13) % 14 != 0) newAvailableEdges.add(rightTopEdge)
            if (leftBottomEdge in 0 until 196 && !isBitSet(board.boardGrid,leftBottomEdge)
                && index % 14 != 0 ) newAvailableEdges.add(leftBottomEdge)
            if (rightBottomEdge in 0 until 196 && !isBitSet(board.boardGrid,rightBottomEdge)
                && (index-13) % 14 != 0) newAvailableEdges.add(rightBottomEdge)
        }
        val filteredEdges = newAvailableEdges.filter { edge ->
            val cellAbove = edge - 14
            val cellLeft = edge - 1
            val cellRight = edge + 1
            val cellBelow = edge + 14

            val aboveOk = cellAbove !in 0 until 196 || !isBitSet(player.bitBoard,cellAbove)
            val leftOk = cellLeft !in 0 until 196 || !isBitSet(player.bitBoard,cellLeft)
            val rightOk = cellRight !in 0 until 196 || !isBitSet(player.bitBoard,cellRight)
            val belowOk = cellBelow !in 0 until 196 || !isBitSet(player.bitBoard,cellBelow)

            aboveOk && leftOk && rightOk && belowOk
        }

        return filteredEdges.toSet()
    }

    fun notCheckForNotAvailableEdges(edges: Set<Int>, board: BlokusBoard):Set<Int>{
        val notAvailableEdges : MutableSet<Int> = mutableSetOf()
        edges.forEach { index->
            if (isBitSet(board.boardGrid, index)) notAvailableEdges.add(index)
        }
        return notAvailableEdges
    }

    fun calculateAllMovesOfAPlayer(player: Player, board: BlokusBoard, rules: GameRules):Set<Move>{
        val validMoves = mutableListOf<Move>()
//        val timeTaken = measureTime {
            for (polyomino in player.polyominos) {
                // Transformationen: Rotationen & Spiegelungen
                val transformedShapes = polyomino.distinctVariants
//            Log.d("AppViewModel", "getAllTransformations: $timeTaken2")
                for (shape in transformedShapes) {
                    for (edge in player.availableEdges) {
                        // Probiere alle Verschiebungen des Polyominos von der Edge aus
                        for (cell in shape) {
                            val newShape = normalizeShapeForCell(cell, shape)
                            val boardPositions = newShape.map {
                                it + edge
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
                                    Move(
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
        //}
//            Log.d("AppViewModel", "validMoves ${validMoves.size}")
           // Log.d("AppViewModel", "time taken $timeTaken")
        return validMoves.toSet()
    }

    private fun normalizeShapeForCell(cell: Int, shape: List<Int>): List<Int> {
        return shape.map { index ->
            index - cell
        }
    }


    fun calculateNotAvailableMoves(player: Player, board: BlokusBoard):List<Move>{

        var notValidMoves = listOf<Move>()
        //val timeTaken = measureTime {
            val lastPlacedPolyominoFromPlayer =
                board.placedPolyominos.lastOrNull { it.playerId == player.id }
            //val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()
            if (lastPlacedPolyominoFromPlayer == null) return emptyList()

            //funktioniert nicht bei mehr als 2 Spielern
            val lastPlacedPolyominoFromOtherPlayer =
                board.placedPolyominos.lastOrNull { it.playerId != player.id }
            //val lastPlacedPolyominoFromOtherPlayer =
            //    lastPlacedPolyominosFromOtherPlayer.lastOrNull()

            val notAvailableEdges: MutableList<Int> =
                mutableListOf(lastPlacedPolyominoFromPlayer.placePosition)

            if (lastPlacedPolyominoFromOtherPlayer != null) {
                lastPlacedPolyominoFromOtherPlayer.cells.forEach {
                    notAvailableEdges.add(it)
                }
            }
            lastPlacedPolyominoFromPlayer.cells.forEach {
                notAvailableEdges.add(it + 1)
                notAvailableEdges.add(it - 1)
                notAvailableEdges.add(it + 14)
                notAvailableEdges.add(it - 14)
            }

            notValidMoves = player.availableMoves.filter { move ->
                notAvailableEdges.contains(move.position) ||
                        move.orientation.any { cell ->
                            cell in notAvailableEdges
                        } || move.polyomino.name == lastPlacedPolyominoFromPlayer.polyomino.name
            }
        //}
        //Log.d("AppViewModel", "not validMoves  $timeTaken")
        //Log.d("AppViewModel", "not validMoves fun1 ${notValidMoves.size}")
        return notValidMoves
    }

    fun calculateNewMoves(player: Player, board: BlokusBoard, rules: GameRules): List<Move> {
        val validMoves = mutableListOf<Move>()
        //val timeTaken = measureTime {

        val newEdges = calculateNewAvailableEdges(player, board)

        val lastPlacedPolyominosFromPlayer = board.placedPolyominos.filter { it.playerId == player.id }
        val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()

        if (lastPlacedPolyominoFromPlayer == null) return emptyList()

        for (polyomino in player.polyominos) {
            // Transformationen: Rotationen & Spiegelungen
            val transformedShapes = polyomino.distinctVariants
            for (shape in transformedShapes) {
                for (edge in newEdges) {
                    // Probiere alle Verschiebungen des Polyominos von der Edge aus
                    for (cell in shape) {
                        val newShape = normalizeShapeForCell(cell, shape)
                        val boardPositions = newShape.map {
                            it + edge
                        }

                        // Ist der Zug erlaubt?
                        if (rules.isValidPlacement(player, boardPositions, board, edge)) {
                            validMoves.add(
                                Move(
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
//        }
//        Log.d("AppViewModel", "calculate newMoves $timeTaken")
//        Log.d("AppViewModel", "new validMoves ${validMoves.size}")
        return validMoves
    }
    fun checkForNotValidMoves( moves :Set<Move>,rules: GameRules,player: Player,board: BlokusBoard):List<Move> {
        val notValidMoves = mutableListOf<Move>()
        for ( move in moves) {
            if (!rules.isValidPlacement(player, move.polyomino.cells, board, move.position)) {
                notValidMoves.add(move)
            }
        }
        return notValidMoves
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
