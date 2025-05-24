package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.model.Move
import kotlin.time.measureTime

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
        val placedPoly = PlacedPolyomino(player.id, polyomino, placedCells, Pair(col,row))
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

    fun calculateAllMovesOfAPlayer(player: Player,board: GameBoard,rules: GameRules):Set<Move>{
        val validMoves = mutableListOf<Move>()
//        val timeTaken = measureTime {

            for (polyomino in player.polyominos) {
                // Transformationen: Rotationen & Spiegelungen
                val transformedShapes = polyomino.getAllTransformations()

                for (shape in transformedShapes) {
                    for (edge in player.availableEdges) {
                        // Probiere alle Verschiebungen des Polyominos von der Edge aus
                        for (cell in shape) {
                            val newShape = normalizeShapeForCell(cell, shape)
                            val edgeX = edge % board.boardSize
                            val edgeY: Int = edge / board.boardSize

                            // Ist der Zug erlaubt?
                            if (rules.isValidPlacement(
                                    player,
                                    newShape,
                                    board,
                                    Pair(edgeX, edgeY)
                                )
                            ) {
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
        //}
//            Log.d("AppViewModel", "validMoves ${validMoves.size}")
//            Log.d("AppViewModel", "Time taken to calculate all moves: $timeTaken ")

        return validMoves.toSet()
    }

    private fun normalizeShapeForCell(cell: Pair<Int,Int>, shape: List<Pair<Int,Int>>):List<Pair<Int,Int>>  {
        return shape.map { (x,y)->
            Pair(x-cell.first,y-cell.second)
        }
    }

    fun calculateNotAvailableMoves2(player: Player, validMoves: Set<Move>,board: GameBoard,rules: GameRules): Set<Move> {
        val notAvailableMoves = mutableSetOf<Move>()
        validMoves.forEach { move->
            if(!rules.isValidPlacement(player,move.orientation,board,move.position)){
                notAvailableMoves.add(move)
            }
            if (move.polyomino.name == board.placedPolyominos.last().polyomino.name){
                notAvailableMoves.add(move)
            }
        }
  //      Log.d("AppViewModel", "not validMoves fun2 ${notAvailableMoves.size}")
        return notAvailableMoves
    }
    fun calculateNotAvailableMoves(player: Player, board: GameBoard):List<Move>{
        var notValidMoves = listOf<Move>()

//        val timeTaken = measureTime {
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
                notAvailableEdges.add(Pair(it.first + 1, it.second))
                notAvailableEdges.add(Pair(it.first - 1, it.second))
                notAvailableEdges.add(Pair(it.first, it.second + 1))
                notAvailableEdges.add(Pair(it.first, it.second - 1))
            }
            notValidMoves = player.availableMoves.filter { move ->
                notAvailableEdges.contains(move.position) ||
                        move.orientation.any { cell ->
                            cell.copy(
                                first = cell.first + move.position.first,
                                second = cell.second + move.position.second
                            ) in notAvailableEdges
                        } || move.polyomino.name == lastPlacedPolyominoFromPlayer.polyomino.name

            }
//        }
        //Log.d("AppViewModel", "not validMoves fun1 ${notValidMoves.size}")
        //Log.d("AppViewModel", "Time taken calculate notAvailable moves: $timeTaken")
        return notValidMoves
    }
//    fun calculateNotAvailableMovesOptimized(player: Player, board: GameBoard): Set<Move> {
//        val notValidMoves = mutableSetOf<Move>() // Use a Set for the result as well
//        val timeTaken = measureTime { // Use measureTimeMillis
//
//            // 1. Find the last polyomino placed by the current player
//            val lastPlacedByPlayer = board.placedPolyominos.lastOrNull { it.playerId == player.id }
//                ?: return emptySet() // If player hasn't placed anything, no moves become invalid based on this logic
//
//            // --- Optimization: Pre-calculate forbidden cells using a Set ---
//            val forbiddenCells = mutableSetOf<Pair<Int, Int>>()
//
//            // 2. Add cells from the player's last placed piece
//            // Original code added only placePosition, which seems incorrect.
//            // Let's assume we should forbid placing *on* any cell of the last piece.
//            forbiddenCells.addAll(lastPlacedByPlayer.cells)
//
//            // 3. Add cells from ALL opponent pieces (Fixes > 2 player issue and "only last" issue)
//            board.placedPolyominos.forEach { placedPiece ->
//                if (placedPiece.playerId != player.id) {
//                    forbiddenCells.addAll(placedPiece.cells)
//                }
//            }
//            // Note: Depending on game rules, you might only need cells placed *after* the player's last move,
//            // or *all* occupied cells regardless of player. The logic above assumes any opponent cell is forbidden.
//            // Adjust this part based on your specific game's rules for invalidation.
//
//            // --- Optimization: Filter using Set lookups ---
//            for (move in player.availableMoves) {
//                var isInvalid = false
//
//                // Condition 1: Polyomino shape already used in the last move
//                if (move.polyomino.name == lastPlacedByPlayer.polyomino.name) {
//                    isInvalid = true
//                } else {
//                    // Condition 2: Placement position itself is forbidden (optional based on rules)
//                    // Your original code had this check. It might be redundant if condition 3 is comprehensive.
//                    // Keep it if placing the *anchor* on a specific forbidden cell is disallowed independently.
//                    // if (forbiddenCells.contains(move.position)) {
//                    //     isInvalid = true
//                    // } else {
//
//                    // Condition 3: Any cell of the move overlaps with forbidden cells
//                    for (cellOffset in move.orientation) {
//                        val absoluteCellPosition = Pair(
//                            cellOffset.first + move.position.first,
//                            cellOffset.second + move.position.second
//                        )
//                        // --- Performance Gain: O(1) average time lookup ---
//                        if (forbiddenCells.contains(absoluteCellPosition)) {
//                            isInvalid = true
//                            break // No need to check further cells for this move
//                        }
//                    }
//                    // } // End of else for condition 2
//                }
//
//
//                if (isInvalid) {
//                    notValidMoves.add(move)
//                }
//            }
//        } // End of measureTimeMillis
//
//        //Log.d("AppViewModel", "Time taken to calculate notAvailable moves (Optimized): $timeTaken ms")
//        Log.d("AppViewModel", "Not valid moves count fun3: ${notValidMoves.size}")
//        return notValidMoves
//    }

    fun calculateNewMoves(player: Player, board: GameBoard, rules: GameRules): List<Move> {
        val validMoves = mutableListOf<Move>()
//        val timeTaken = measureTime {
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
//        }
//        Log.d("AppViewModel", "Time taken calculate new moves: $timeTaken")
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
