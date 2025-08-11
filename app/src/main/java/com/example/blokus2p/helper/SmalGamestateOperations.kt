package com.example.blokus2p.helper

import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameRules
import com.example.blokus2p.model.GameState
import com.example.blokus2p.model.Polyomino
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
            val cellAbove = index - INDEX_TOP
            val cellLeft = index - INDEX_LEFT
            val cellRight = index + INDEX_RIGHT
            val cellBelow = index + INDEX_BOTTOM
            //Probleme mit den Rändern ?
            if (cellAbove in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellAbove)) notAvailableEdges.add(index)
            if (cellLeft in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellLeft) && index % ROW_SIZE != 1) notAvailableEdges.add(index)
            if (cellRight in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellRight) && index % ROW_SIZE != 14) notAvailableEdges.add(index)
            if (cellBelow in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD && isBitSet(playerBoard, cellBelow)) notAvailableEdges.add(index)
        }
    }
    return notAvailableEdges
}



fun calculateNewMoves(player: SmalPlayer, board: SmalBoard, rules: GameRules): List<SmalMove> {
    val validMoves = mutableListOf<SmalMove>()
    //val timeTaken = measureTime {

    val newEdges = player.availableEdges

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
        if (it % ROW_SIZE != 14) notAvailableEdges.add(it + INDEX_RIGHT)
        if (it % ROW_SIZE != 1) notAvailableEdges.add(it - INDEX_LEFT)
        if (it % ROW_SIZE != 14) notAvailableEdges.add(it + INDEX_BOTTOM)
        if (it % ROW_SIZE != 1) notAvailableEdges.add(it - INDEX_TOP)
    }

    val notValidMoves = player.availableMoves.filter { move ->
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

fun checkForNotValidMoves( moves :Set<SmalMove>, currentMove: SmalMove,rules: GameRules,player: SmalPlayer,board: SmalBoard):List<SmalMove> {
    val notValidMoves = mutableListOf<SmalMove>()
    for ( move in moves) {
        if (!rules.isValidPlacementSmal(player, move.polyomino.cells, board ) || move.polyomino.name == currentMove.polyomino.name ) {
            notValidMoves.add(move)
        }
    }
    return notValidMoves
}

fun makeMove(gameState: SmalGameState, move: SmalMove, player: SmalPlayer): SmalGameState  {
    val rules = BlokusRules()
    //println(move.polyomino.cells)
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

//            val allAvailableMoves = calculateAllMovesOfAPlayer(
//                updatedPlayer.copy(availableEdges = finalEdges), newBoard, rules)
            val newAvailableMoves = calculateNewMoves(
                updatedPlayer.copy(availableEdges = finalEdges), newBoard, rules)
            val notValidMoves = checkForNotValidMoves(p.availableMoves, move, rules, updatedPlayer, newBoard)

            // println("not valid moves: ${notValidMoves.size}, not available moves: ${notAvailableMoves.size}, not available moves2: ${notAvailbaleMoves2.size}")
            val finalMoves = updatedPlayer.availableMoves + newAvailableMoves - notValidMoves.toSet()
            //println("all available : ${allAvailableMoves.size}, final: ${finalMoves.size} new available: ${newAvailableMoves.size}, not available: ${notAvailableMoves.size},")
//            val movesNotinAllMoves: MutableList<SmalMove> = mutableListOf()
//            if (finalMoves.size != allAvailableMoves.size) {
//                allAvailableMoves.forEach { move ->
//                    if (!finalMoves.contains(move))
//                        movesNotinAllMoves.add(move)
//                        //println(move)
//                }
//            }
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

fun evaluate(gameState: SmalGameState,maximizingPlayerId:Int): Int {
    val maximizingPlayer = getPlayerById(gameState,maximizingPlayerId)
    val opponent = gameState.players[maximizingPlayerId  % gameState.players.size]

    // Grundwertung: Punktedifferenz
    var score = (maximizingPlayer.points - opponent.points) * 3
    score += (maximizingPlayer.availableMoves.size - opponent.availableMoves.size)

    //Kontrolle über das Zentrum bewerten
    val centerControl = evaluateCenterControl(maximizingPlayer, opponent, gameState)
    score += centerControl
    return score
}

fun evaluateCenterControl(
    maximizingPlayer: SmalPlayer,
    opponent: SmalPlayer,
    gameState: SmalGameState
): Int {
    val lastPolyominoMaximizingPlayer =
        gameState.board.placedPolyominosSmal.lastOrNull { it.playerId == maximizingPlayer.id }
    val lastPolyominoOpponent =
        gameState.board.placedPolyominosSmal.lastOrNull { it.playerId == opponent.id }

    if(lastPolyominoMaximizingPlayer != null && lastPolyominoOpponent != null) {
        val distanceMaximizingPlayer = distancToCenterForPolyomino(lastPolyominoMaximizingPlayer)
        val distanceOpponent = distancToCenterForPolyomino(lastPolyominoOpponent)
        return (-distanceMaximizingPlayer + distanceOpponent).toInt()
    }else if (lastPolyominoMaximizingPlayer == null && lastPolyominoOpponent != null) {
        return distancToCenterForPolyomino(lastPolyominoOpponent).toInt()  // Wenn einer der Spieler noch keinen Zug gemacht hat, gibt es keine Bewertung
    }else if (lastPolyominoMaximizingPlayer != null) {
        return -distancToCenterForPolyomino(lastPolyominoMaximizingPlayer).toInt()  // Wenn einer der Spieler noch keinen Zug gemacht hat, gibt es keine Bewertung
    }else {
        throw IllegalStateException("Both players must have made a move to evaluate center control")
    }
}

fun distancToCenterForPolyomino(
    polyomino: PlacedSmalPolyomino
): Double {
    val centerX = 7.5
    val centerY = 7.5
    var distance = 0.0
    polyomino.cells.forEach { cell ->
        val x = cell % ROW_SIZE
        val y = cell / COLUM_SIZE
        distance += Math.abs(x - centerX) + Math.abs(y- centerY)
    }
    return distance
}

fun getPlayerById(gameState: SmalGameState, playerId: Int): SmalPlayer {
    return gameState.players.firstOrNull { it.id == playerId }
        ?: throw IllegalArgumentException("Player with id $playerId not found")
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


