package com.example.blokus2p.ai

import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move

class MinmaxAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        TODO("Not yet implemented")
    }


    fun gameOver(board: GameBoard): Boolean {
        // Implement game over logic here
        return false
    }

    fun evaluate(board: GameBoard): Int {
        // Implement evaluation function to rate board positions
        return 0
    }

    fun getMoves(board: GameBoard): List<Move> {
        // Generate possible moves for the current board state
        return listOf()
    }

    fun makeMove(board: GameBoard, move: Move): GameBoard {
        // Apply the move to the board and return a new board state
        return board
    }


    fun minimax(board: GameBoard, depth: Int): Int {
        if (depth == 0 || gameOver(board)) {
            return evaluate(board)
        }

        var bestScore = if (isMaximizingPlayer(board)) Integer.MIN_VALUE else Integer.MAX_VALUE
        for (move in getMoves(board)) {
            val newBoard = makeMove(board, move)
            val score = minimax(newBoard, depth - 1)
            if (isMaximizingPlayer(board)) {
                bestScore = maxOf(bestScore, score)
            } else {
                bestScore = minOf(bestScore, score)
            }
        }
        return bestScore
    }

    fun isMaximizingPlayer(board: GameBoard): Boolean {
        // Implement logic to determine if the current player is maximizing (e.g., based on turn order)
        return true
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

//    fun minimax(board: Board, depth: Int): ScoredMove {
//        if (depth == 0 || gameOver(board)) {
//            return ScoredMove(null, evaluate(board))
//        }
//
//        var bestScore = if (isMaximizingPlayer(board)) Integer.MIN_VALUE else Integer.MAX_VALUE
//        var bestMove: Move? = null
//
//        for (move in getMoves(board)) {
//            val newBoard = makeMove(board, move)
//            val scoredMove = minimax(newBoard, depth - 1)
//            val score = scoredMove.score
//
//            if (isMaximizingPlayer(board)) {
//                if (score > bestScore) {
//                    bestScore = score
//                    bestMove = move
//                }
//            } else {
//                if (score < bestScore) {
//                    bestScore = score
//                    bestMove = move
//                }
//            }
//        }
//
//        return ScoredMove(bestMove, bestScore)
//    }

}