package com.example.blokus2p.model

import com.example.blokus2p.ai.AiInterface
import com.example.blokus2p.ai.RandomAi

enum class PlayerType {
    Human,
    MinimaxAI,
    RandomAI,
    MonteCarloAI;
}