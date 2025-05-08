package com.example.blokus2p.ai

import android.util.Log
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move
import kotlin.random.Random

class RandomAi:AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        val moves = GameEngine().calculateAllMovesOfAPlayer(gameState.activPlayer,gameState.board,BlokusRules())
        if (moves.isNotEmpty()){
            val sortedMoves = moves.sortedBy { move ->
                move.polyomino.points
            }
            val newMoves = sortedMoves.filter { move->
                move.polyomino.points == moves.first().polyomino.points
            }
            //Log.d("AppViewModel", "validMoves2 ${newMoves.size}")
            return newMoves.randomOrNull()
        }
        return moves.randomOrNull() // wählt zufällig einen legalen Zug
    }

}