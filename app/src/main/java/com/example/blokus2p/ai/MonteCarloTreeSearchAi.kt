package com.example.blokus2p.ai

import android.util.Log
import com.example.blokus2p.game.GameState
import com.example.blokus2p.helper.gameStateToSmalGameState
import com.example.blokus2p.helper.smalMoveToMove
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.Node
import com.example.blokus2p.model.SmalGameState

class MonteCarloTreeSearchAi : AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        val smalGameState = gameStateToSmalGameState(gameState)
        return mctsSearch(smalGameState, 15000)
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
            val result = node.rollout()
            Log.d("AppViewModel", "Backpropagating result: $result for move: ${node.move}")

            // 4. Backpropagation
            node.backpropagate(result)
            Log.d("AppViewModel", "Backpropagating result: $result for move: ${node.move}")
        }

        return smalMoveToMove(
            root.children.maxByOrNull { it.visits }?.move
                ?: throw IllegalStateException("No moves available")
        )

    }

}