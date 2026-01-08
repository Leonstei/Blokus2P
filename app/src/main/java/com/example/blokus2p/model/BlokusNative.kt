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
    external fun initGame(): ByteArray
    external fun getLegalActions(stateBytes : ByteArray, player: Int): IntArray
    external fun applyAction(stateBytes : ByteArray, actionId: Int): ByteArray
    external fun isTerminal(stateBytes : ByteArray): Boolean
    external fun getBestMoveMinimax(stateBytes : ByteArray, player: Int, depth: Int): Long
    external fun getBestMoveMcts(stateBytes : ByteArray, player: Int, iterations: Int): Long
    // Füge mehr hinzu, z. B. für AlphaZero
}