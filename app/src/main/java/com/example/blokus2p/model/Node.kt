package com.example.blokus2p.model

import android.util.Log
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.helper.getActivPlayer
import com.example.blokus2p.helper.makeMove

data class Node(
    val state: SmalGameState,
    val parent: Node?,
    val move: SmalMove?,
    var visits: Int = 0,
    var wins: Double = 0.0,
    val children: MutableList<Node> = mutableListOf(),
    val untriedMoves: MutableList<SmalMove> =  getActivPlayer(state).availableMoves.toMutableList()
){
    fun uctSelectChild(): Node {
        val c = 1.41  // Exploration-Konstante
        return children.maxByOrNull {
            if (it.visits == 0) Double.MAX_VALUE
            else (it.wins / it.visits) + c * Math.sqrt(Math.log(visits.toDouble()) / it.visits)
        } ?: throw IllegalStateException("No children to select")
    }

    fun expand(): Node {
        val move = untriedMoves.removeAt(0)
        val nextState = makeMove(state,move, getActivPlayer(state))
        val child = Node(state = nextState, parent = this, move = move)
        children.add(child)
        return child
    }
    fun rollout(): Double {
        var rolloutState = state.copy()  // Deep copy, um originalen Baum nicht zu verändern
        while (!rolloutState.isFinished) {
            val activPlayer = getActivPlayer(rolloutState)
            //Log.d("AppViewModel", "Rollout state: {${rolloutState.board.placedPolyominos}")
            val moves = activPlayer.availableMoves
            val randomMove = moves.random()
            //Log.d("AppViewModel", "Random move: $randomMove")
            rolloutState = makeMove(rolloutState,randomMove,activPlayer)
            if(GameEngine().checkForGameEnd(rolloutState.players))
                rolloutState = rolloutState.copy(isFinished = true)
        }
        return rolloutState.getResult()  // z. B. 1.0 für Sieg, 0.0 für Niederlage
    }
    fun backpropagate(result: Double) {
        var current: Node? = this
        while (current != null) {
            current.visits += 1
            current.wins += result
            current = current.parent
        }
    }

}