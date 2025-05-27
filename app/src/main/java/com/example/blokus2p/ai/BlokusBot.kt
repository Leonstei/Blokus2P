package com.example.blokus2p.ai

import com.example.blokus2p.game.GameState
import com.example.blokus2p.model.PolyominoNames

data class Move(val pieceId: Int, val position : Int, val rotation: Int, val flip: Boolean)
// Du wirst wahrscheinlich schon eine Move-Klasse in deinem Projekt haben

class BlokusBot {

    companion object {
        init {
            // Lade die native Bibliothek. Der Name muss mit add_library in CMakeLists.txt übereinstimmen.
            System.loadLibrary("blokus_mcts")
        }
    }

    /**
     * Ruft den MCTS-Algorithmus in C++ auf, um den besten Zug zu finden.
     *
     * @param board      Das 14x14 Spielfeld, repräsentiert als flaches 1D-Array (400 Elemente).
     * Jeder Wert repräsentiert den Spieler, dem das Feld gehört (0 für leer, 1 für Spieler 1, etc.).
     * @param playerBitBoards Ein Array von Longs, das die Bitboards für jeden Spieler enthält.
     * @param piecesAvailable Für jeden Spieler ein Boolean-Array, das angibt, welche Steine noch verfügbar sind.
     * Dimensionen: [Anzahl der Spieler][Anzahl der Steine].
     * Blokus hat 21 Steine pro Spieler, also [2][21] für 2 Spieler.
     * @param currentPlayer Der Index des Spielers, der am Zug ist (0, 1, 2, 3).
     * @param timeoutMillis Die maximale Zeit in Millisekunden, die der MCTS-Algorithmus laufen darf.
     * @return Ein Array von Integern, das den besten Zug repräsentiert: [pieceId, row, col, rotation, flip]
     * Wenn kein Zug gefunden wird oder ein Fehler auftritt, kann null zurückgegeben werden.
     */
    external fun findBestMove(
        board: LongArray,
        playerBitBoards: Array<LongArray>,
        piecesAvailable: Array<BooleanArray>,
        currentPlayer: Int,
        timeoutMillis: Long
    ): IntArray?

    // Hilfsfunktion, um das IntArray in ein Move-Objekt zu konvertieren
    fun getNextMoveFromMCTS(gameState: GameState, timeoutMillis: Long): Move? {
        val boardArray = gameState.board.boardGrid
        val playerBitBoards : Array<LongArray> = gameState.players.map { it.bitBoard }.toTypedArray()
        val piecesAvailableArray = Array(gameState.players.size) { playerIndex ->
            BooleanArray(21) { pieceId ->
                gameState.players[playerIndex].polyominos.any { it.name.id == pieceId }
                //gameState.players[playerIndex].polyominos.contains(pieceId) // Angenommen pieceId ist ein Int
            }
        }

        val moveResult = findBestMove(
            board = boardArray,
            playerBitBoards = playerBitBoards,
            piecesAvailable = piecesAvailableArray,
            currentPlayer = gameState.activPlayer_id, // Angenommen Player IDs sind 1-basiert
            timeoutMillis = timeoutMillis
        )

        return if (moveResult != null && moveResult.size == 5) {
            Move(
                pieceId = moveResult[0],
                position = moveResult[1],
                rotation = moveResult[3], // Angenommen, Rotation ist an Index 3
                flip = moveResult[4] == 1 // Angenommen, Flip ist 1 für true und 0 für false
            )
        } else {
            null
        }
    }
}