package com.example.blokus2p.ai

import android.util.Log
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.ScoredMove
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class MinmaxAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        var depth = 3
        if(gameState.activPlayer.availableMoves.size > 50){
            depth--
        }
        val bestMove = findBestMove(gameState, depth)
        return bestMove
    }

    private suspend fun findBestMoveParallel(
        gameState: GameState,
        depth: Int
    ): Move? = coroutineScope {
        val maximizingPlayer = getMaximizingPlayer(gameState)
        val currentPlayer = gameState.players[gameState.activPlayer_id - 1]
        val moves = getMoves(currentPlayer)
        val currentdepth = depth-1


        val semaphore = Semaphore(permits = 4) // max 8 gleichzeitig

        val deferredResults = moves.map { move ->
            async(Dispatchers.Default) {
                semaphore.withPermit {
                    val newState = makeMove(gameState, move, currentPlayer)
                    minmaxAlphaBeta(currentdepth, newState, maximizingPlayer, Int.MIN_VALUE, Int.MAX_VALUE)
                        .copy(move = move)
                }
            }
        }

        val results = deferredResults.awaitAll()
        val best = if (currentPlayer.id == maximizingPlayer.id) {
            results.maxByOrNull { it.score }
        } else {
            results.minByOrNull { it.score }
        }
        best?.move
    }
    private fun findBestMove(gameState: GameState, depth: Int): Move? {
        val maximizingPlayer = getMaximizingPlayer(gameState)
        val scoredMove = minmaxAlphaBeta(depth, gameState,maximizingPlayer,Int.MIN_VALUE, Int.MAX_VALUE)
        //Log.d("AppViewModel", "Score: ${scoredMove.score}")
        return scoredMove.move
    }

    fun getMoves(player: Player): Set<Move> {
        return player.availableMoves
    }

    fun makeMove(gameState: GameState, move: Move,player: Player): GameState  {

        val rules = BlokusRules()
        val newBoard = GameEngine().placeAiMove(
            player,
            move.polyomino,
            move.position.first,
            move.position.second,gameState.board, rules, move.orientation
        ) ?: return gameState

        val updatedPlayers = gameState.players.map { p ->
            if (p.id == player.id) {
                val newPolyominos = p.polyominos.toMutableList().apply { remove(move.polyomino) }

                val newAvailableEdges = GameEngine().calculateNewAvailableEdges(p, newBoard)
                val notAvailableEdges = GameEngine().notCheckForNotAvailableEdges(p.availableEdges, newBoard)
                val finalEdges = p.availableEdges + newAvailableEdges - notAvailableEdges

                val newAvailableMoves = GameEngine().calculateNewMoves(
                    p.copy(availableEdges = finalEdges, polyominos = newPolyominos), newBoard, rules)
                val notAvailableMoves = GameEngine().calculateNotAvailableMoves(p, newBoard)
                val finalMoves = p.availableMoves + newAvailableMoves - notAvailableMoves.toSet()
                p.copy(
                    points = p.points + move.polyomino.points,
                    polyominos = newPolyominos,
                    isActiv = false,
                    availableEdges = finalEdges,
                    availableMoves = finalMoves
                )
            } else {
                val opponentNotAvailableMoves = GameEngine().calculateNotAvailableMoves(p, newBoard)
                p.copy(isActiv = true, availableMoves = p.availableMoves.minus(opponentNotAvailableMoves.toSet())) // Unverändert übernehmen
            }
        }

        return gameState.copy(
            activPlayer_id = if (gameState.activPlayer_id == gameState.players.size) 1 else gameState.activPlayer_id + 1,
            board = newBoard,
            players = updatedPlayers
        )
    }


    fun minmaxAlphaBeta(depth: Int, gameState: GameState, maximizingPlayer: Player,alpha:Int,beta:Int): ScoredMove {
        // Basisfall: Bei Erreichung der maximalen Tiefe oder Spielende
        if (depth == 0 || gameOver(gameState)) {
            return ScoredMove(
                move = null,
                score = evaluate(gameState),
                depth = depth
            )
        }

        var currentAlpha = alpha
        var currentBeta = beta
        var bestMove: Move? = null

        val currentPlayer = gameState.players[gameState.activPlayer_id-1]
        val isMaximizingPly = (currentPlayer.id == maximizingPlayer.id)

        val possibleMoves = getMoves(currentPlayer)
        val filterdMoves = if (possibleMoves.size > 200){
            possibleMoves.filterIndexed { index, move ->
                index % 4 == 0 && move.polyomino.points == possibleMoves.first().polyomino.points
            }
        }else {
            possibleMoves.filterIndexed { index, move ->
                index % 3 == 0 && move.polyomino.points == possibleMoves.first().polyomino.points
            }
        }

        if(isMaximizingPly){
            var maxScore = Int.MIN_VALUE // Bester Wert für diesen maximierenden Knoten
            for (move in filterdMoves) {
                val newGameState = makeMove(gameState, move, currentPlayer)
                val scoredMoveRecursive = minmaxAlphaBeta(depth - 1, newGameState, maximizingPlayer, currentAlpha, currentBeta)
                val score = scoredMoveRecursive.score // Erhaltener Score vom Kindknoten

                if (score > maxScore) {
                    maxScore = score
                    bestMove = move
                }
                currentAlpha = maxOf(currentAlpha, maxScore) // Alpha aktualisieren: Bester Wert, den der Maximierer bisher auf diesem Pfad garantieren kann

                if (beta <= currentAlpha) { // Pruning-Bedingung
                    //Log.d("AppViewModel", "Moves Skiped: ${possibleMoves.size- possibleMoves.indexOf(move)}")
                    break // Beta-Cutoff: Der Minimierer-Elternknoten wird diesen Pfad nicht wählen
                }
            }
            return ScoredMove(bestMove, maxScore, depth)
        }else { // Minimierender Spieler ist am Zug
            var minScore = Int.MAX_VALUE // Bester Wert für diesen minimierenden Knoten
            for (move in possibleMoves) {
                val newGameState = makeMove(gameState, move, currentPlayer)
                // Rekursiver Aufruf für den nächsten Zustand, alpha und beta weitergeben
                val scoredMoveRecursive = minmaxAlphaBeta(depth - 1, newGameState, maximizingPlayer, currentAlpha, currentBeta)
                val score = scoredMoveRecursive.score // Erhaltener Score vom Kindknoten

                if (score < minScore) {
                    minScore = score
                    bestMove = move
                }
                currentBeta = minOf(currentBeta, minScore) // Beta aktualisieren: Bester Wert, den der Minimierer bisher auf diesem Pfad garantieren kann (aus Maximierer-Sicht niedrig)

                if (currentBeta <= alpha) { // Pruning-Bedingung
                    //Log.d("AppViewModel", "Moves Skiped: ${possibleMoves.size- possibleMoves.indexOf(move)}")
                    break // Alpha-Cutoff: Der Maximierer-Elternknoten wird diesen Pfad nicht wählen
                }
            }
            return ScoredMove(bestMove, minScore, depth)
        }
    }


    fun getMaximizingPlayer(gameState: GameState): Player {
        gameState.players.forEach {player->
            if (player.isMaximizing)
                return player
        }
        return Player()
    }

    fun gameOver(gameState: GameState): Boolean {
        return GameEngine().checkForGameEnd(gameState.players)
    }

    fun evaluate(gameState: GameState): Int {
        val maximizingPlayer = getMaximizingPlayer(gameState)
        val opponent = gameState.players[maximizingPlayer.id  % gameState.players.size]

        // Grundwertung: Punktedifferenz
        var score = (maximizingPlayer.points - opponent.points) * 3
        score += (maximizingPlayer.availableMoves.size - opponent.availableMoves.size)

        //Kontrolle über das Zentrum bewerten
        val centerControl = evaluateCenterControl(maximizingPlayer, opponent, gameState)
        score += centerControl
        return score
    }

    fun evaluateCenterControl(
        maximizingPlayer: Player,
        opponent: Player,
        gameState: GameState
    ): Int {
        val lastPolyominoMaximizingPlayer = gameState.board.placedPolyominos.filter { it.playerId == maximizingPlayer.id }.last()
        val lastPolyominoOpponent = gameState.board.placedPolyominos.filter { it.playerId == opponent.id }.last()
        val centerX = 6.5
        val centerY = 6.5
        var distanceMaximizingPlayer = 0.0
        lastPolyominoMaximizingPlayer.cells.forEach { cell->
            distanceMaximizingPlayer += Math.abs(cell.first - centerX) +
                    Math.abs(cell.second - centerY)
        }
        var distanceOpponent = 0.0
        lastPolyominoOpponent.cells.forEach { cell->
            distanceOpponent += Math.abs(cell.first - centerX) +
                    Math.abs(cell.second - centerY)
        }
        return (-distanceMaximizingPlayer + distanceOpponent).toInt()
    }

//    fun minimax(board: Board, depth: Int, alpha: Int, beta: Int, isMaximizingPlayer: Boolean): Int {
//        if (depth == 0 || gameOver(board)) {
//            return evaluate(board)
//        }
//
//        if (isMaximizingPlayer) {
//            var bestScore = Integer.MIN_VALUE
//            for (move in getMoves(board)) {
//                val newBoard = makeMove(board, move)
//                val score = minimax(newBoard, depth - 1, alpha, beta, false)
//                bestScore = maxOf(bestScore, score)
//                alpha = maxOf(alpha, bestScore)
//                if (beta <= alpha) {
//                    break
//                }
//            }
//            return bestScore
//        } else {
//            var bestScore = Integer.MAX_VALUE
//            for (move in getMoves(board)) {
//                val newBoard = makeMove(board, move)
//                val score = minimax(newBoard, depth - 1, alpha, beta, true)
//                bestScore = minOf(bestScore, score)
//                beta = minOf(beta, bestScore)
//                if (beta <= alpha) {
//                    break
//                }
//            }
//            return bestScore
//        }
//    }

}