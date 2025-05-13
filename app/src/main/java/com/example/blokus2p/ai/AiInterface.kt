package com.example.blokus2p.ai

import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.Move2

interface AiInterface {
    suspend fun getNextMove(gameState: GameState): Move2?

//    fun boardUpdate()
}