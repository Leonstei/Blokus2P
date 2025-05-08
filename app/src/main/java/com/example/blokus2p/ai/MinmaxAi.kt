package com.example.blokus2p.ai

import android.util.Log
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.ScoredMove
import kotlin.time.measureTime

class MinmaxAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        val depth = 1 // Set the depth for the minimax algorithm
        val bestMove = findBestMove(gameState, depth)
        return bestMove
    }

    private fun findBestMove(gameState: GameState, depth: Int): Move? {
        val maximizingPlayer = maximizingPlayer(gameState)
        val scoredMove = minimax(depth, gameState,maximizingPlayer)
        //Log.d("AppViewModel", "Score: ${scoredMove.score}")
        return scoredMove.move
    }

    fun getMoves(player: Player): Set<Move> {
        return player.availableMoves
    }

    fun makeMove(gameState: GameState, move: Move,player: Player): GameState  {
        var newBoard: GameBoard
        var updatedPlayers: List<Player>

        val rules = BlokusRules()
        newBoard = GameEngine().placeAiMove(
            player,
            move.polyomino,
            move.position.first,
            move.position.second,gameState.board, rules, move.orientation
        ) ?: return gameState

         updatedPlayers = gameState.players.map { p ->
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
                // Neuen Player zurückgeben

            } else {
                p.copy(isActiv = true) // Unverändert übernehmen
            }
        }

        return gameState.copy(
            activPlayer_id = if (gameState.activPlayer_id == gameState.players.size) 1 else gameState.activPlayer_id + 1,
            board = newBoard,
            players = updatedPlayers
        )
    }


    fun minimax(depth: Int, gameState: GameState,maximizingPlayer: Player): ScoredMove {
        // Basisfall: Bei Erreichung der maximalen Tiefe oder Spielende
        if (depth == 0 || gameOver(gameState)) {
            return ScoredMove(
                move = null,
                score = evaluate(gameState),
                depth = depth
            )
        }

        var bestScore = if (maximizingPlayer.id == gameState.activPlayer_id) Int.MIN_VALUE else Int.MAX_VALUE
        var bestMove: Move? = null

        val currentPlayer = gameState.players[gameState.activPlayer_id-1]
        val possibleMoves = getMoves(currentPlayer)

        for (move in possibleMoves) {
            val newGameState = makeMove(gameState, move, currentPlayer)
            // Hier war ein Fehler: Wir müssen newGameState verwenden, nicht gameState
            val scoredMove = minimax(depth - 1, newGameState,maximizingPlayer)
            val score = scoredMove.score

            if ((maximizingPlayer.id == gameState.activPlayer_id && score > bestScore) ) {
                bestScore = score
                bestMove = move
            }
        }

        return ScoredMove(
            move = bestMove,
            score = bestScore,
            depth = depth
        )
    }

    fun maximizingPlayer(gameState: GameState): Player {
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
        val maximizingPlayer = maximizingPlayer(gameState)
        val opponent = gameState.players[maximizingPlayer.id  % gameState.players.size]

        // Grundwertung: Punktedifferenz
        var score = maximizingPlayer.points - opponent.points
        score += (maximizingPlayer.availableMoves.size - opponent.availableMoves.size)

        //Kontrolle über das Zentrum bewerten
        val centerControl = evaluateCenterControl(maximizingPlayer, opponent, gameState)
        score += centerControl * 2

        // Implement evaluation function to rate board positions
        //Log.d("AppViewModel", "Score: $score")
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