package com.example.blokus2p

import android.util.Log
import com.example.blokus2p.ai.Move
import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.PlacedPolyomino
import com.example.blokus2p.game.Player
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.helper.gameStateToSmalGameState
import com.example.blokus2p.helper.getActivPlayer
import com.example.blokus2p.helper.makeMove
import com.example.blokus2p.helper.setBit
import com.example.blokus2p.model.PlacedSmalPolyomino
import com.example.blokus2p.model.PolyominoNames
import com.example.blokus2p.model.SmalBoard
import com.example.blokus2p.model.SmalMove
import com.example.blokus2p.model.SmalPlayer
import com.example.blokus2p.model.SmalPolyomino
import com.example.blokus2p.viewModel.AppViewModel
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.assertEquals


class GameEngineTest {

    val player = SmalPlayer(1,  availableEdges = setOf(130))
    val selectedPosition = 130
    val gameEngine = GameEngine()
    val board = SmalBoard()
    val viewModel = AppViewModel()
    val gameState = viewModel.gameState.value



    @Test
    fun `makeMove gibt unveränderten GameState bei ungültigem Zug zurück`() {
        val einnerPolyomino = SmalPolyomino(PolyominoNames.EINS, 1,  cells=listOf(10))
        // placeAiMove gibt null zurück -> ungültiger Zug
        val move = SmalMove(
            einnerPolyomino,
            10,
        )

        val result = makeMove(gameStateToSmalGameState(gameState) , move, player)
        assertEquals(gameStateToSmalGameState(gameState).board.boardGrid.get(0) , result.board.boardGrid.get(0))
        assertEquals(gameStateToSmalGameState(gameState).board.boardGrid.get(1) , result.board.boardGrid.get(1))
        assertEquals(gameStateToSmalGameState(gameState).board.boardGrid.get(2) , result.board.boardGrid.get(2))
        assertEquals(gameStateToSmalGameState(gameState).board.boardGrid.get(3) , result.board.boardGrid.get(3))
    }

    @Test
    fun `makeMove aktualisiert Spieler und Board korrekt bei gültigem Zug`() {
        val einnerPolyomino = SmalPolyomino(PolyominoNames.EINS, 1,  cells=listOf(130))
        // placeAiMove gibt ein neues Board zurück
        setBit(board.boardGrid, selectedPosition)
        val oldBoard =  board.copy(placedPolyominosSmal = listOf(PlacedSmalPolyomino(1, einnerPolyomino, einnerPolyomino.cells,selectedPosition)))
        // Simuliere weitere Methoden, falls nötig
        val move = SmalMove(
            einnerPolyomino,
            10,
        )

        val result = makeMove(gameStateToSmalGameState(gameState) , move, player)
        // Überprüfe, ob das Board und die Spieler aktualisiert wurden
        println("new Board: ${result.board}")
        println("Old Board: ${oldBoard}")
        assertEquals(oldBoard.boardGrid.get(2), result.board.boardGrid.get(2))
        // Weitere Assertions je nach Logik
        println("Placed Polyominos: ${result.board.placedPolyominosSmal}")
        assertEquals(1,result.board.placedPolyominosSmal.size)
    }

    @Test
    fun `makeMove wechselt aktiven Spieler korrekt`() {

        val einnerPolyomino = SmalPolyomino(PolyominoNames.EINS, 1,  cells=listOf(130))
        // placeAiMove gibt ein neues Board zurück
        setBit(board.boardGrid, selectedPosition)
        val oldBoard =  board.copy(placedPolyominosSmal = listOf(PlacedSmalPolyomino(1, einnerPolyomino, einnerPolyomino.cells,selectedPosition)))
        // Simuliere weitere Methoden, falls nötig
        val move = SmalMove(
            einnerPolyomino,
            10,
        )

        val result = makeMove(gameStateToSmalGameState(gameState) , move, player)

        assertNotEquals(gameState.activPlayer.id, getActivPlayer(result).id)
    }

    @Test
    fun `rollout mit makeMove`() {
        var rolloutState = gameStateToSmalGameState(viewModel.gameState.value ) // Deep copy, um originalen Baum nicht zu verändern
        while (!rolloutState.isFinished) {
            //Log.d("AppViewModel", "Rollout state: {${rolloutState.board.placedPolyominos}")
            val activPlayer = getActivPlayer(rolloutState)
            val moves = activPlayer.availableMoves
            if (moves.isEmpty()) {
                break
            }
            val randomMove = moves.random()
            rolloutState = makeMove(rolloutState,randomMove,activPlayer)
            if(gameEngine.checkForGameEnd(rolloutState.players))
                rolloutState = rolloutState.copy(isFinished = true)
        }

//        val result = rolloutState.getResult()
//        println(result)
        assertEquals(true, rolloutState.board.placedPolyominosSmal.isNotEmpty())
    }

    // Weitere Tests für Spezialfälle, z.B. keine Polyominos mehr, keine Moves mehr etc.

}