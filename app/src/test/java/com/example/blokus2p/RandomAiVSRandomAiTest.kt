package com.example.blokus2p

import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.events.PolyominoEvent
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.PlayerType
import com.example.blokus2p.viewModel.AppViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RandomAiVSRandomAiTest {

//    private val testDispatcher = StandardTestDispatcher()
//    private val testScope = TestScope(testDispatcher)
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain() // Setzt den Main-Dispatcher zurück
//    }
    @Test
    fun testRandomAiVsRandomAi() {
        var playerOneWins = 0
        var playerTwoWins = 0
        repeat(1000) {
            val viewModel: AppViewModel = AppViewModel()
            val gameState = viewModel.gameState.value
            viewModel.onEvent(
                GameEvent.ChangePlayerSettings(
                    gameState.players[0].name, gameState.players[1].name,
                    gameState.players[0].color, gameState.players[1].color,
                    PlayerType.RandomAI, PlayerType.RandomAI
                )
            )

            while (viewModel.gameState.value.isFinished == false) {
                // Simulate player 1's turn
                viewModel.checkForAiTurn()
//                println("Player 1 points: ${viewModel.gameState.value.players[0].points}")
                // Simulate player 2's turn
                viewModel.checkForAiTurn()
//                println("Player 2 points: ${viewModel.gameState.value.players[1].points}")
            }
            val newGameState = viewModel.gameState.value
//            for (player in newGameState.players) {
//                println("Player ${player.id} points: ${player.points}")
//            }
            val playerOne = newGameState.players.first { player: Player ->
                player.id == 1
            }
            val playerTwo = newGameState.players.first { player: Player ->
                player.id == 2
            }
            if(playerOne.points > playerTwo.points) {
                playerOneWins++
                println("Player 1 wins with ${playerOne.points} points")
            } else if(playerOne.points < playerTwo.points) {
                playerTwoWins++
                println("Player 2 wins with ${playerTwo.points} points")
            } else {
                println("It's a draw with ${playerOne.points} points each")
            }
            assertTrue(viewModel.gameState.value.isFinished)
        }
        println("Player 1 wins: $playerOneWins")
        println("Player 2 wins: $playerTwoWins")
    }

}