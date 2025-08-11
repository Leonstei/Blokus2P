package com.example.blokus2p.game

import com.example.blokus2p.helper.*
import com.example.blokus2p.helper.clearBit
import com.example.blokus2p.helper.isBitSet
import com.example.blokus2p.helper.setBit
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.PlacedPolyomino
import com.example.blokus2p.model.Player
import com.example.blokus2p.model.Polyomino
import com.example.blokus2p.model.SmalPlayer

class GameEngine {
    fun place(
        player: Player,
        polyomino: Polyomino,
        position: Int,
        board: GameBoard,
        rules: GameRules
    ): GameBoard? {
        if (!rules.isValidPlacement(player, polyomino.cells, board)) {
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

    fun placeAiMove(player: Player, polyomino: Polyomino, position:Int, board: GameBoard, rules: GameRules, orientation: List<Int>) : GameBoard?{
        val newBoard =place(player,polyomino.copy(cells = orientation),position,board,rules)
        return newBoard
    }

    fun undoplace(board: GameBoard):GameBoard?{
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

    fun calculateNewAvailableEdges(player: Player, board: GameBoard): Set<Int> {
        val newAvailableEdges: MutableSet<Int> = mutableSetOf()
        val lastPlacedPolyominoFromPlayer = board.placedPolyominos.lastOrNull { it.playerId == player.id }
        //val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()

        if (lastPlacedPolyominoFromPlayer == null) return setOf(START_INDEX_PLAYER1,START_INDEX_PLAYER2)

        lastPlacedPolyominoFromPlayer.cells.forEach { index ->
            val leftTopEdge = index - INDEX_TOP_LEFT
            val rightTopEdge = index - INDEX_TOP_RIGHT
            val leftBottomEdge = index + INDEX_BOTTOM_LEFT
            val rightBottomEdge = index + INDEX_BOTTOM_RIGHT
            if (leftTopEdge in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && !isBitSet(board.boardGrid,leftTopEdge)
                && index % ROW_SIZE != 1) newAvailableEdges.add(leftTopEdge)
            if (rightTopEdge in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && !isBitSet(board.boardGrid,rightTopEdge)
                && index % ROW_SIZE != 14) newAvailableEdges.add(rightTopEdge)
            if (leftBottomEdge in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && !isBitSet(board.boardGrid,leftBottomEdge)
                && index % ROW_SIZE != 1 ) newAvailableEdges.add(leftBottomEdge)
            if (rightBottomEdge in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && !isBitSet(board.boardGrid,rightBottomEdge)
                && index % ROW_SIZE != 14) newAvailableEdges.add(rightBottomEdge)
        }
        val filteredEdges = newAvailableEdges.filter { edge ->
            val cellAbove = edge - INDEX_TOP
            val cellLeft = edge - INDEX_LEFT
            val cellRight = edge + INDEX_RIGHT
            val cellBelow = edge + INDEX_BOTTOM

            val aboveOk = cellAbove !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD || !isBitSet(player.bitBoard,cellAbove)
            val leftOk = cellLeft !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD || !isBitSet(player.bitBoard,cellLeft)
            val rightOk = cellRight !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD || !isBitSet(player.bitBoard,cellRight)
            val belowOk = cellBelow !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD || !isBitSet(player.bitBoard,cellBelow)

            aboveOk && leftOk && rightOk && belowOk
        }

        return filteredEdges.toSet()
    }

    fun checkForNotAvailableEdges(edges: Set<Int>, board: GameBoard, playerBoard: LongArray):Set<Int>{
        val notAvailableEdges : MutableSet<Int> = mutableSetOf()
        edges.forEach { index->
            if (isBitSet(board.boardGrid, index)) notAvailableEdges.add(index)
            else{
                val cellAbove = index - INDEX_TOP
                val cellLeft = index - INDEX_LEFT
                val cellRight = index + INDEX_RIGHT
                val cellBelow = index + INDEX_BOTTOM

                if (cellAbove in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellAbove)) notAvailableEdges.add(index)
                if (cellLeft in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellLeft) && index % ROW_SIZE != 1) notAvailableEdges.add(index)
                if (cellRight in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellRight) && index % 14 != 13) notAvailableEdges.add(index)
                if (cellBelow in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellBelow)) notAvailableEdges.add(index)
            }
        }
        return notAvailableEdges
    }

    fun calculateAllMovesOfAPlayer(player: Player, board: GameBoard, rules: GameRules):Set<Move>{
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
                                    board
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


    fun calculateNotAvailableMoves(player: Player, board: GameBoard):List<Move>{

        var notValidMoves: List<Move>
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
                if (it % ROW_SIZE != 14) notAvailableEdges.add(it + INDEX_RIGHT)
                if (it % ROW_SIZE != 1) notAvailableEdges.add(it - INDEX_LEFT)
                if (it % ROW_SIZE != 14) notAvailableEdges.add(it + INDEX_BOTTOM)
                if (it % ROW_SIZE != 1) notAvailableEdges.add(it - INDEX_TOP)
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

    fun calculateNewMoves(player: Player, board: GameBoard, rules: GameRules): List<Move> {
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
                        if (rules.isValidPlacement(player, boardPositions, board)) {
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
    fun checkForNotValidMoves(moves :Set<Move>, placedPolyomino: Polyomino, rules: GameRules, player: Player, board: GameBoard):List<Move> {
        val notValidMoves = mutableListOf<Move>()
        for ( move in moves) {
            if (!rules.isValidPlacement(player, move.orientation, board  )|| move.polyomino.name == placedPolyomino.name) {
                notValidMoves.add(move)
            }
        }
        return notValidMoves
    }




    fun checkForGameEnd(players :List<SmalPlayer>): Boolean {
        var countPlayersFinished= 0
        for (player in players) {
            if (player.polyominos.isEmpty() || player.availableEdges.isEmpty() || player.availableMoves.isEmpty()) {
                countPlayersFinished ++
            }
        }
        return countPlayersFinished == players.size
    }
}
