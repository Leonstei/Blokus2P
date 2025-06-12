package com.example.blokus2p.ai

import android.util.Log
import com.example.blokus2p.game.GameState
import com.example.blokus2p.helper.evaluate
import com.example.blokus2p.helper.gameStateToSmalGameState
import com.example.blokus2p.helper.smalMoveToMove
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.Node
import com.example.blokus2p.model.SmalGameState

class MonteCarloTreeSearchAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        val smalGameState = gameStateToSmalGameState(gameState)
        return mctsSearch(smalGameState, 3000)
    }

    fun mctsSearch(rootState: SmalGameState, timeLimitMs: Long): Move {
        val root = Node(state = rootState, parent = null, move = null)
        val endTime = System.currentTimeMillis() + timeLimitMs

        while (System.currentTimeMillis() < endTime) {
            var node = root

            // 1. Selection
            while (node.untriedMoves.isEmpty() && node.children.isNotEmpty()) {
                node = node.uctSelectChild()
            }

            // 2. Expansion
            if (node.untriedMoves.isNotEmpty()) {
                node = node.expand()
            }

            // 3. Simulation
            val result = node.rollout(node.state.activPlayer_id)
//            Log.d("AppViewModel", "Backpropagating result: $result for move: ${node.move}")
            //println("Simulated result: $result for move: ${node.move}")

            // 4. Backpropagation
            node.backpropagate(result)
 //           Log.d("AppViewModel", "Backpropagating result: $result for move: ${node.move}")
            //println("Backpropagating result: $result for move: ${node.move}")
        }
        //println("MCTS completed with ${root.visits} visits")

//        for (child in root.children) {
//            println("Move=${child.move}, visits=${child.visits}, winRate=${child.wins / child.visits}")
//        }


        val bestMoves = root.children.filter { (it.wins / it.visits) == root.children.maxOfOrNull { child -> (child.wins / child.visits) }}
        val bestMove = bestMoves.maxByOrNull { evaluate(it.state, root.state.activPlayer_id) }
//        println("Best move found with ${root.visits} visits, win rate: ${bestMove?.wins?.div(bestMove.visits)} move: ${bestMove?.move}")
//
//        for (move in bestMoves) {
//            println("Move=$move")
//        }
//        println()
        return smalMoveToMove(bestMove?.move ?: throw IllegalStateException("No best move found"))
//        return smalMoveToMove(
//            root.children.maxByOrNull { it.wins / it.visits }?.move
//                ?: throw IllegalStateException("No moves available")
//        )

    }

}