package com.example.blokus2p.ai

import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move
import kotlin.random.Random

class RandomAi:AiInterface {
    fun pickMove( moves : List<Move>):Move{
        val randomInt =  Random.nextInt(0, moves.size)
        return moves[randomInt]
    }
    override fun getNextMove(gameState: GameState): Move? {
        val moves = GameEngine().calculateAllMovesOfAPlayer(gameState.activPlayer,gameState.board,BlokusRules())
        return moves.randomOrNull() // wählt zufällig einen legalen Zug
    }
}