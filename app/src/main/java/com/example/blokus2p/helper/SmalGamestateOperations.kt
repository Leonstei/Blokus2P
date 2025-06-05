package com.example.blokus2p.helper

import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameRules
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.PlacedPolyomino
import com.example.blokus2p.game.Player
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.PlacedSmalPolyomino
import com.example.blokus2p.model.SmalBoard
import com.example.blokus2p.model.SmalGameState
import com.example.blokus2p.model.SmalMove
import com.example.blokus2p.model.SmalPlayer
import com.example.blokus2p.model.SmalPolyomino

fun gameStateToSmalGameState(gameState: GameState): SmalGameState {
    return SmalGameState(
        players = gameState.players.map { player ->
            SmalPlayer(
                id = player.id,
                isActiv = player.isActiv,
                isMaximizing = player.isMaximizing,
                points = player.points,
                bitBoard = player.bitBoard,
                polyominos = player.polyominos.map { polyomino ->
                    SmalPolyomino(
                        name = polyomino.name,
                        points = polyomino.points,
                        cells = polyomino.cells
                    )
                },
                availableEdges = player.availableEdges,
                availableMoves = player.availableMoves.map { move ->
                    SmalMove(
                        polyomino = SmalPolyomino(
                            name = move.polyomino.name,
                            points = move.polyomino.points,
                            cells = move.orientation
                        ),
                        position = move.position
                    )
                }.toSet()
            )
        },
        activPlayer_id = gameState.activPlayer_id,
        isFinished = gameState.isFinished,
        board = boardToSmalBoard(gameState.board)
    )
}
fun smalMoveToMove(smalMove: SmalMove): Move {
    return Move(
        polyomino = Polyomino(
            name = smalMove.polyomino.name,
            points = smalMove.polyomino.points,
            cells = smalMove.polyomino.cells
        ),
        orientation = smalMove.polyomino.cells,
        position = smalMove.position
    )

}
fun boardToSmalBoard(board: GameBoard): SmalBoard {
    return SmalBoard(
        boardGrid = board.boardGrid.copyOf(),
        placedPolyominosSmal = board.placedPolyominos.map { placedPoly ->
            PlacedSmalPolyomino(
                playerId = placedPoly.playerId,
                polyomino = SmalPolyomino(
                    name = placedPoly.polyomino.name,
                    points = placedPoly.polyomino.points,
                    cells = placedPoly.polyomino.cells
                ),
                cells = placedPoly.cells,
                placePosition = placedPoly.placePosition
            )
        })
}



fun place(
    player: SmalPlayer,
    polyomino: SmalPolyomino,
    position: Int,
    board: SmalBoard,
    rules: GameRules
): SmalBoard? {
    if (!rules.isValidPlacementSmal(player, polyomino.cells, board)) {
        return null
    }
    val newGrid = board.boardGrid.copyOf()
    polyomino.cells.forEach {
        setBit(newGrid, it)
    }
    val placedPoly = PlacedSmalPolyomino(player.id, polyomino, polyomino.cells, position)
    return board.copyWith(
        boardGrid = newGrid,
        placedPolyominos = board.placedPolyominosSmal + placedPoly,
    )
}

fun calculateAllMovesOfAPlayer(player: SmalPlayer, board: SmalBoard, rules: GameRules):Set<SmalMove>{
    val validMoves = mutableListOf<SmalMove>()
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
                    if (rules.isValidPlacementSmal(
                            player,
                            boardPositions,
                            board
                        )
                    ) {
                        validMoves.add(
                            SmalMove(
                                polyomino = polyomino.copy(cells = boardPositions),
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

fun calculateNewAvailableEdges(player: SmalPlayer, board: SmalBoard): Set<Int> {
    val newAvailableEdges: MutableSet<Int> = mutableSetOf()
    val lastPlacedPolyominoFromPlayer = board.placedPolyominosSmal.lastOrNull { it.playerId == player.id }
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
fun notCheckForNotAvailableEdges(edges: Set<Int>, board: SmalBoard,playerBoard: LongArray):Set<Int>{
    val notAvailableEdges : MutableSet<Int> = mutableSetOf()
    edges.forEach { index->
        if (isBitSet(board.boardGrid, index)) notAvailableEdges.add(index)
        else{
            val cellAbove = index - 14
            val cellLeft = index - 1
            val cellRight = index + 1
            val cellBelow = index + 14

            if (cellAbove in 0 until 196 && isBitSet(playerBoard, cellAbove)) notAvailableEdges.add(index)
            if (cellLeft in 0 until 196 && isBitSet(playerBoard, cellLeft)) notAvailableEdges.add(index)
            if (cellRight in 0 until 196 && isBitSet(playerBoard, cellRight)) notAvailableEdges.add(index)
            if (cellBelow in 0 until 196 && isBitSet(playerBoard, cellBelow)) notAvailableEdges.add(index)
        }
    }
    return notAvailableEdges
}



fun calculateNewMoves(player: SmalPlayer, board: SmalBoard, rules: GameRules): List<SmalMove> {
    val validMoves = mutableListOf<SmalMove>()
    //val timeTaken = measureTime {

    val newEdges = calculateNewAvailableEdges(player, board)

    val lastPlacedPolyominosFromPlayer = board.placedPolyominosSmal.filter { it.playerId == player.id }
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
                    if (rules.isValidPlacementSmal(player, boardPositions, board)) {
                        validMoves.add(
                            SmalMove(
                                polyomino = polyomino.copy(cells = boardPositions),
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
fun calculateNotAvailableMoves(player: SmalPlayer, board: SmalBoard):List<SmalMove>{

    var notValidMoves: List<SmalMove>
    //val timeTaken = measureTime {
    val lastPlacedPolyominoFromPlayer =
        board.placedPolyominosSmal.lastOrNull { it.playerId == player.id }
    //val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()
    if (lastPlacedPolyominoFromPlayer == null) return emptyList()

    //funktioniert nicht bei mehr als 2 Spielern
    val lastPlacedPolyominoFromOtherPlayer =
        board.placedPolyominosSmal.lastOrNull { it.playerId != player.id }
    //val lastPlacedPolyominoFromOtherPlayer =
    //    lastPlacedPolyominosFromOtherPlayer.lastOrNull()

    val notAvailableEdges: MutableList<Int> =
        mutableListOf(lastPlacedPolyominoFromPlayer.placePosition)

    if (lastPlacedPolyominoFromOtherPlayer != null) {
        lastPlacedPolyominoFromOtherPlayer.cells.forEach {
            notAvailableEdges.add(it)
        }
    }
    lastPlacedPolyominoFromPlayer.cells.forEach { // hier muss ich auf die Zellen achten die über den Rand gehen
        notAvailableEdges.add(it + 1)
        notAvailableEdges.add(it - 1)
        notAvailableEdges.add(it + 14)
        notAvailableEdges.add(it - 14)
    }

    notValidMoves = player.availableMoves.filter { move ->
        notAvailableEdges.contains(move.position) ||
                move.polyomino.cells.any { cell ->
                    notAvailableEdges.contains(cell)
                } ||
                move.polyomino.name == lastPlacedPolyominoFromPlayer.polyomino.name
        // letzte abfrage kann ich mir vielleicht sparen
    }
    //}
    //Log.d("AppViewModel", "not validMoves  $timeTaken")
    //Log.d("AppViewModel", "not validMoves fun1 ${notValidMoves.size}")
    return notValidMoves
}
fun calculateNotAvailableMoves2(player: SmalPlayer, board: SmalBoard):List<SmalMove>{

    var notValidMoves: List<SmalMove>
    //val timeTaken = measureTime {
    val lastPlacedPolyominoFromPlayer =
        board.placedPolyominosSmal.lastOrNull { it.playerId == player.id }
    //val lastPlacedPolyominoFromPlayer = lastPlacedPolyominosFromPlayer.lastOrNull()
    if (lastPlacedPolyominoFromPlayer == null) return emptyList()

    //funktioniert nicht bei mehr als 2 Spielern
    val lastPlacedPolyominoFromOtherPlayer =
        board.placedPolyominosSmal.lastOrNull { it.playerId != player.id }
    //val lastPlacedPolyominoFromOtherPlayer =
    //    lastPlacedPolyominosFromOtherPlayer.lastOrNull()

    val notAvailableEdges: MutableList<Int> =
        mutableListOf(lastPlacedPolyominoFromPlayer.placePosition)

    if (lastPlacedPolyominoFromOtherPlayer != null) {
        lastPlacedPolyominoFromOtherPlayer.cells.forEach {
            notAvailableEdges.add(it)
        }
    }
    lastPlacedPolyominoFromPlayer.cells.forEach { // hier muss ich auf die Zellen achten die über den Rand gehen
        notAvailableEdges.add(it + 1)
        notAvailableEdges.add(it - 1)
        notAvailableEdges.add(it + 14)
        notAvailableEdges.add(it - 14)
    }

    notValidMoves = player.availableMoves.filter { move ->
        notAvailableEdges.contains(move.position) ||
                move.polyomino.cells.any { cell ->
                    notAvailableEdges.contains(cell)
                } ||
                move.polyomino.name == lastPlacedPolyominoFromPlayer.polyomino.name
        // letzte abfrage kann ich mir vielleicht sparen
    }
    //}
    //Log.d("AppViewModel", "not validMoves  $timeTaken")
    //Log.d("AppViewModel", "not validMoves fun1 ${notValidMoves.size}")
    return notValidMoves
}

fun makeMove(gameState: SmalGameState, move: SmalMove, player: SmalPlayer): SmalGameState  {
    val rules = BlokusRules()
    println(move.polyomino.cells)
    val newBoard = place(
        player,
        move.polyomino,
        move.position,
        gameState.board, rules
    )
        ?: return gameState


    val updatedPlayers = gameState.players.map { p ->
        if (p.id == player.id) {
            val newPolyominos = p.polyominos.toMutableList().apply { removeIf { move.polyomino.name == it.name } }

            val updatedPlayerBitBoard = getUpdatedPlayerBitBoard(gameState.board.boardGrid, newBoard.boardGrid,p.bitBoard)

            val updatedPlayer =  p.copy(polyominos = newPolyominos, bitBoard = updatedPlayerBitBoard)
            val newAvailableEdges = calculateNewAvailableEdges(updatedPlayer, newBoard)
            val notAvailableEdges = notCheckForNotAvailableEdges(updatedPlayer.availableEdges, newBoard,updatedPlayerBitBoard)
            val finalEdges = p.availableEdges + newAvailableEdges - notAvailableEdges

            val allAvailableMoves = calculateAllMovesOfAPlayer(
                updatedPlayer.copy(availableEdges = finalEdges), newBoard, rules)
            val newAvailableMoves = calculateNewMoves(
                updatedPlayer.copy(availableEdges = finalEdges), newBoard, rules)
            val notAvailableMoves = calculateNotAvailableMoves(updatedPlayer, newBoard)
            val finalMoves = updatedPlayer.availableMoves + newAvailableMoves - notAvailableMoves.toSet()
            //println("all available : ${allAvailableMoves.size}, final: ${finalMoves.size} new available: ${newAvailableMoves.size}, not available: ${notAvailableMoves.size},")
            val movesNotinAllMoves: MutableList<SmalMove> = mutableListOf()
            if (finalMoves.size != allAvailableMoves.size) {
                allAvailableMoves.forEach { move ->
                    if (!finalMoves.contains(move))
                        movesNotinAllMoves.add(move)
                        //println(move)
                }
            }
            updatedPlayer.copy(
                points = p.points + move.polyomino.points,
                isActiv = false,
                availableEdges = finalEdges,
                availableMoves = finalMoves
            )
        } else {
            val opponentNotAvailableMoves = calculateNotAvailableMoves(p, newBoard)
            p.copy(isActiv = true, availableMoves = p.availableMoves.minus(opponentNotAvailableMoves.toSet())) // Unverändert übernehmen
        }
    }

    return gameState.copy(
        activPlayer_id = if (gameState.activPlayer_id == gameState.players.size) 1 else gameState.activPlayer_id + 1,
        board = newBoard,
        players = updatedPlayers
    )
}
fun getActivPlayer(gameState: SmalGameState): SmalPlayer {
    return gameState.players.firstOrNull { it.isActiv }
        ?: throw IllegalStateException("No active player found in the game state")
}

private fun normalizeShapeForCell(cell: Int, shape: List<Int>): List<Int> {
    return shape.map { index ->
        index - cell
    }
}


