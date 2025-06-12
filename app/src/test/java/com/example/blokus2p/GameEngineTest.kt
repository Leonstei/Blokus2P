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
import kotlin.time.measureTime


class GameEngineTest {

    val player = SmalPlayer(1,  availableEdges = setOf(130))
    val selectedPosition = 130
    val gameEngine = GameEngine()
    val board = SmalBoard()
    val viewModel = AppViewModel()
    val gameState = viewModel.gameState.value
    val edgesOfFirstMoves =
        listOf(
            2,4,4,4,4,4,4,4,4,2,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,

            7,7,7,7,7,7,7,7,
            7,7,7,7,7,7,7,7,
            7,7,7,7,7,7,7,7,
            7,7,7,7,7,7,7,7,
            7,7,7,7,7,7,7,7,

            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,

            7,7,7,7,7,7,7,7,
            7,7,7,7,7,7,7,7,
            7,7,7,7,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,

            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,

            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,

            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,
            6,6,6,6,

            8,8,8,8,8,

            4,4,4,4,4,4,4,4,

            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,
            5,5,5,5,5,5,5,5,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,

            6,6,6,6,6,6,6,6,
            6,6,6,6,6,6,6,6,

            4,4,4,4,

            4,4,4,4,4,4,

            5,5,5,5,5,5,5,5,
            5,5,5,5,

            4,4,4,4,

            4)
    //[130, 131, 144, 145, 158]
    //[37, 51, 65]
    //[91, 104, 105, 106, 118]
    //[67, 80, 81, 82]
    //[61, 74, 75, 89]
    //[7, 21, 22]
    //[4, 18, 32, 33, 46]
    //[12, 26, 27, 40, 54]
    //[99, 100, 101, 115]
    //[93, 107]
    //[64, 78, 79]
    //[109, 122, 123, 124, 136]
    //[57, 70, 71, 84]
    val moves = listOf(
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_BLOCK, 5,
                cells = listOf(130, 131, 144, 145, 158) ), 130
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.DREI, 3,
                cells = listOf(37, 51, 65) ), 130
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_7, 5,
                cells = listOf(91, 104, 105, 106, 118) ), 130
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_T, 4,
                cells = listOf(67, 80, 81, 82) ), 80
        ),        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_T, 4,
                cells = listOf(61, 74, 75, 89) ), 89
        ),        SmalMove(
            SmalPolyomino( PolyominoNames.DREI_L, 3,
                cells = listOf(7, 21, 22) ), 22
        ),        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_SMAL_T, 5,
                cells = listOf(4, 18, 32, 33, 46) ), 46
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_SMAL_T, 5,
                cells = listOf(12, 26, 27, 40, 54) ), 54
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_L, 4,
                cells = listOf(99, 100, 101, 115) ), 115
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.ZWEI, 2,
                cells = listOf(93, 107) ), 93
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.DREI_L, 3,
                cells = listOf(64, 78, 79) ), 78
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_7, 5,
                cells = listOf(109, 122, 123, 124, 136) ), 130
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_Z, 4,
                cells = listOf(57, 70, 71, 84) ), 84
        ),
    )
    val moves2 = listOf(
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_PLUS, 5,
                cells = listOf(116, 129, 130, 131, 144) ), 130
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF, 5,
                cells = listOf(65, 79, 93, 107, 121) ), 65
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_ZL, 5,
                cells = listOf(134, 135, 146, 147, 148) ), 146
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_ZL, 5,
                cells = listOf(7, 21, 35, 36, 50) ), 50
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_SMAL_T, 5,
                cells = listOf(80, 94, 108, 109, 122) ), 122
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_7, 5,
                cells = listOf(38, 52, 53, 54, 67) ), 52 //2
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_W, 5,
                cells = listOf(71, 85, 86, 100, 101) ), 101
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_L, 5,
                cells = listOf(136, 150, 164, 178, 179) ), 136
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF, 5,
                cells = listOf(47, 61, 75, 89, 103) ), 103
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_SMAL_T, 5,
                cells = listOf(48, 62, 63, 76, 90) ), 48
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_T, 5,
                cells = listOf(3, 4, 5, 18, 32) ), 32
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_C, 5,
                cells = listOf(9, 10, 11, 23, 25) ), 23
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_L, 5,
                cells = listOf(77, 91, 105, 118, 119) ), 119
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_W, 5,
                cells = listOf(69, 82, 83, 95, 96) ), 69
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_Z, 5,
                cells = listOf(163, 175, 176, 177, 189) ), 163
        ),

        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_T, 4,
                cells = listOf(153, 166, 167, 181) ), 166
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_7, 5,
                cells = listOf(126, 140, 141, 142, 155) ), 142 //1
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.EINS, 1,
                cells = listOf(194) ), 194
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_BLOCK, 5,
                cells = listOf(43, 44, 45, 58, 59) ), 58
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.ZWEI, 2,
                cells = listOf(19, 33) ), 33
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_T, 4,
                cells = listOf(137, 138, 139, 152) ), 137
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_L, 4,
                cells = listOf(111, 123, 124, 125) ), 111
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.DREI, 3,
                cells = listOf(159, 173, 187) ), 159
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_LANG_L, 5,
                cells = listOf(46, 60, 72, 73, 74) ), 46
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.VIER_L, 4,
                cells = listOf(170, 182, 183, 184) ), 170
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.FÜNF_Z, 5,
                cells = listOf(1, 15, 16, 17, 31) ), 31
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.ZWEI, 2,
                cells = listOf(14, 28) ), 28
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.DREI_L, 5,
                cells = listOf(42, 56, 57) ), 57
        ),
        SmalMove(
            SmalPolyomino( PolyominoNames.EINS, 1,
                cells = listOf(98) ), 98
        ),
    )



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
    @Test
    fun `make all first moves and calculateNewAvailableMoves`() {
        val gameState = viewModel.gameState.value
        val smalGameState = gameStateToSmalGameState(gameState)
        val playerOne = smalGameState.players.first { it.id == 1 }
        val playerTwo = smalGameState.players.first { it.id == 2 }
        val edgeCounts: MutableList<Int> = mutableListOf()
        playerOne.availableMoves.forEachIndexed { index, move ->
            val newGameState = makeMove(smalGameState, move, playerOne)
            val edgeCount = newGameState.players[0].availableEdges.size
            print(edgeCount)
            edgeCounts.add(newGameState.players[0].availableEdges.size)
            assertEquals(edgesOfFirstMoves[index],newGameState.players[0].availableEdges.size)
        }
    }

    @Test
    fun `make a sertian move and check available edges and moves`() {
        var rolloutState = gameStateToSmalGameState(viewModel.gameState.value )
        moves.forEach {
            val activPlayer = getActivPlayer(rolloutState)
            rolloutState = makeMove(rolloutState,it,activPlayer)
        }
    }

    @Test
    fun `make a sertian move and check available edges and moves 2`() {
        var rolloutState = gameStateToSmalGameState(viewModel.gameState.value )
        moves2.forEach {
            val activPlayer = getActivPlayer(rolloutState)
            rolloutState = makeMove(rolloutState,it,activPlayer)
            if(rolloutState.players[rolloutState.activPlayer_id-1].availableEdges == setOf<Int>(41, 123, 191, 111, 138, 46)){
                println("Found the edge: ${rolloutState.players[rolloutState.activPlayer_id-1].availableEdges}")
            }
            println("Available Edges: ${rolloutState.players[rolloutState.activPlayer_id-1].availableEdges}")
            println("Available Moves: ${rolloutState.players[rolloutState.activPlayer_id-1].availableMoves}")
        }
        println("Available Edges: ${rolloutState.players[1].availableEdges}")
        println("Available Moves: ${rolloutState.players[1].availableMoves}")
    }

    @Test
    fun modOperations(){
        val timeTaken3 = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index % 14 != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations: $timeTaken3")
        val timeTaken2 = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index.mod(14) != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations2: $timeTaken2")
        val timeTaken = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index % 14 != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations: $timeTaken")
        val timeTaken4 = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index.mod(14) != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations2: $timeTaken4")
        val timeTaken5 = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index % 14 != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations: $timeTaken5")
        val timeTaken6 = measureTime {
            var index = 13
            repeat(1000000) {
                val bool =  index.mod(14) != 13
                index++
            }
        }
        println("Time taken for 1000000 iterations2: $timeTaken6")
    }
}