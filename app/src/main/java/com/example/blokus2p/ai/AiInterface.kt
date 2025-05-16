package com.example.blokus2p.ai

import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move

interface AiInterface {
    suspend fun getNextMove(gameState: GameState): Move?

//    fun boardUpdate()
}