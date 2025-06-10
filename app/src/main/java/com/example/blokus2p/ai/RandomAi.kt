package com.example.blokus2p.ai

import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move

class RandomAi:AiInterface {
    override fun getNextMove(gameState: GameState): Move? {
        //val moves = GameEngine().calculateAllMovesOfAPlayer(gameState.activPlayer,gameState.board,BlokusRules())
        val moves = gameState.activPlayer.availableMoves
        if(gameState.activPlayer.id == 1){
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
        }
        return moves.randomOrNull() // wählt zufällig einen legalen Zug
    }

}