package com.example.blokus2p.model

import android.util.Log

object BlokusNative {
    init {
        try {
            System.loadLibrary("native-lib")
            Log.d("BLOKUS_NATIVE", "Native Library geladen")
        } catch (e: Exception) {
            Log.e("BLOKUS_NATIVE", "Fehler beim Laden", e)
        }
    }

    // Testfunktion (bleibt)
    external fun doubleNumber(number: Int): Int

    // Neue Funktionen für Blokus
    external fun initGame(): String  // Gibt initialen State als JSON-String zurück
    external fun getLegalActions(stateJson: String, player: Int): String  // Gibt Liste von Actions als JSON
    external fun applyAction(stateJson: String, actionId: Long, player: Int): String  // Wendet Zug an, gibt neuen State
    external fun isTerminal(stateJson: String): Boolean  // Prüft Endzustand
    external fun getBestMoveMinimax(stateJson: String, player: Int, depth: Int): Long  // Minimax-Bot-Zug
    external fun getBestMoveMcts(stateJson: String, player: Int, iterations: Int): Long  // MCTS-Bot-Zug
    // Füge mehr hinzu, z. B. für AlphaZero
}