package com.example.blokus2p.ai

import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.PlacedPolyomino
import com.example.blokus2p.game.Player
import com.example.blokus2p.helper.gameStateToSmalGameState
import com.example.blokus2p.helper.makeMove
import com.example.blokus2p.helper.smalMoveToMove
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.PlacedSmalPolyomino
import com.example.blokus2p.model.ScoredMove
import com.example.blokus2p.model.SmalGameState
import com.example.blokus2p.model.SmalMove
import com.example.blokus2p.model.SmalPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class MinmaxAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        val smalGameState = gameStateToSmalGameState(gameState)
        var depth = 3
        if(gameState.activPlayer.availableMoves.size > 50){
            depth--
        }
        val bestMove = findBestMove(smalGameState, depth)
        return bestMove
    }

//    private suspend fun findBestMoveParallel(
//        gameState: GameState,
//        depth: Int
//    ): Move? = coroutineScope {
//        val maximizingPlayer = getMaximizingPlayer(gameState)
//        val currentPlayer = gameState.players[gameState.activPlayer_id - 1]
//        val moves = getMoves(currentPlayer)
//
//        val filterdMoves = if (moves.size > 100){
//            moves.filterIndexed { index, move ->
//                index % 4 == 0 && move.polyomino.points == moves.first().polyomino.points
//            }
//        }else {
//            moves.filterIndexed { index, move ->
//                index % 3 == 0 && move.polyomino.points == moves.first().polyomino.points
//            }
//        }
//
//        val moveChunks = filterdMoves.chunked((filterdMoves.size / 10) + 1) // Teilt die Züge in Gruppen von 4 auf
//        val currentdepth = depth-1
//
//        val bestResult = moveChunks.flatMap { chunk ->
//            chunk.map { move ->
//                async(Dispatchers.Default) {
//                    val newState = GameEngine().makeMove(gameState, move, currentPlayer)
//                    minmaxAlphaBeta(currentdepth, newState, maximizingPlayer, Int.MIN_VALUE, Int.MAX_VALUE).copy(move = move)
//                }
//            }.awaitAll()
//        }.maxByOrNull { it.score }
//
//        bestResult?.move


        //val semaphore = Semaphore(permits = 4) // max 8 gleichzeitig
//
//        val deferredResults = moves.map { move ->
//            async(Dispatchers.Default) {
//                semaphore.withPermit {
//                    val newState = makeMove(gameState, move, currentPlayer)
//                    minmaxAlphaBeta(currentdepth, newState, maximizingPlayer, Int.MIN_VALUE, Int.MAX_VALUE)
//                        .copy(move = move)
//                }
//            }
//        }
//
//        val results = deferredResults.awaitAll()
//        val best = if (currentPlayer.id == maximizingPlayer.id) {
//            results.maxByOrNull { it.score }
//        } else {
//            results.minByOrNull { it.score }
//        }
//        best?.move
//    }
    private fun findBestMove(gameState: SmalGameState, depth: Int): Move? {
        val maximizingPlayer = getMaximizingPlayer(gameState)
        val result= minmaxAlphaBeta(depth, gameState,maximizingPlayer.id,Int.MIN_VALUE, Int.MAX_VALUE)
            //Log.d("AppViewModel", "Score: ${scoredMove.score}")
        if( result.move == null) {
            return null
        }else
            return smalMoveToMove(result.move)
    }

    fun getMoves(player: SmalPlayer): Set<SmalMove> {
        return player.availableMoves
    }



    fun minmaxAlphaBeta(
        depth: Int, gameState: SmalGameState, maximizingPlayerId: Int,alpha:Int,beta:Int
    ): ScoredMove {
        // Basisfall: Bei Erreichung der maximalen Tiefe oder Spielende
        if (depth == 0 || gameOver(gameState)) {
            return ScoredMove(
                move = null,
                score = evaluate(gameState, maximizingPlayerId),
                depth = depth
            )
        }

        var currentAlpha = alpha
        var currentBeta = beta
        var bestMove: SmalMove? = null

        val currentPlayer = gameState.players[gameState.activPlayer_id-1]
        val isMaximizingPly = (currentPlayer.id == maximizingPlayerId)
        val possibleMoves = getMoves(currentPlayer)
        val filterdMoves = if (possibleMoves.size > 100){
            possibleMoves.filterIndexed { index, move ->
                index % 3 == 0 && move.polyomino.points == possibleMoves.first().polyomino.points
            }
        }else {
            possibleMoves.filterIndexed { index, move ->
                index % 2 == 0 && move.polyomino.points == possibleMoves.first().polyomino.points
            }
        }

        if (filterdMoves.isEmpty()) {
            // Nächsten Spieler aktivieren (ohne die Tiefe zu verringern, da „kein Zug“)
            val nextPlayerIndex = (gameState.activPlayer_id % gameState.players.size) + 1
            val skippedState = gameState.copy(
                activPlayer_id = nextPlayerIndex
                // Alle anderen Felder bleiben unverändert
            )
            // Rekursiver Aufruf mit gleicher Tiefe (Variante A). Wenn du möchtest,
            // kann hier auch „depth - 1“ stehen, um Skips ebenfalls als „Tiefe“ abzurechnen.
            return minmaxAlphaBeta(depth, skippedState, maximizingPlayerId, currentAlpha, currentBeta)
        }
        if(isMaximizingPly){
            var maxScore = Int.MIN_VALUE // Bester Wert für diesen maximierenden Knoten
            for (move in filterdMoves) {
                val newGameState = makeMove(gameState, move, currentPlayer)
                val scoredMoveRecursive = minmaxAlphaBeta(depth - 1, newGameState, maximizingPlayerId, currentAlpha, currentBeta)
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
            for (move in filterdMoves) {
                val newGameState = makeMove(gameState, move, currentPlayer)
                // Rekursiver Aufruf für den nächsten Zustand, alpha und beta weitergeben
                val scoredMoveRecursive = minmaxAlphaBeta(depth - 1, newGameState, maximizingPlayerId, currentAlpha, currentBeta)
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


    fun getMaximizingPlayer(gameState: SmalGameState): SmalPlayer {
        gameState.players.forEach {player->
            if (player.isActiv)
                return player
        }
        return SmalPlayer()
    }
    fun getPlayerById(gameState: SmalGameState, playerId: Int): SmalPlayer {
        return gameState.players.firstOrNull { it.id == playerId }
            ?: throw IllegalArgumentException("Player with id $playerId not found")
    }

    fun gameOver(gameState: SmalGameState): Boolean {
        return GameEngine().checkForGameEnd(gameState.players)
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
        val centerX = 6.5
        val centerY = 6.5
        var distance = 0.0
        polyomino.cells.forEach { cell ->
            val x = cell % 14
            val y = cell / 14
            distance += Math.abs(x - centerX) + Math.abs(y- centerY)
        }
        return distance
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
//private suspend fun findBestMove(gameState: GameState, depth: Int): Move? {
//    val maximizingPlayer = getMaximizingPlayer(gameState)
//    val result= withContext(Dispatchers.Default) {
//        minmaxAlphaBeta(depth, gameState,maximizingPlayer,Int.MIN_VALUE, Int.MAX_VALUE)
//    }
//    //Log.d("AppViewModel", "Score: ${scoredMove.score}")
//    return result.move
//}

}