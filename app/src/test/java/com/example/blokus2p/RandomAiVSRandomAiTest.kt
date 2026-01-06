package com.example.blokus2p

import com.example.blokus2p.ui.events.GameEvent
import com.example.blokus2p.model.Player
import com.example.blokus2p.helper.PlayerType
import com.example.blokus2p.viewModel.AppViewModel
import junit.framework.TestCase.assertTrue
import kotlin.test.Test

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
//        val availableMovesPerTurn: MutableMap<Int, Pair<Int, Int>> =
//            (1..42).associateWith { 0 to 0 }.toMutableMap()

        repeat(3000) {
            val viewModel: AppViewModel = AppViewModel()
            val gameState = viewModel.gameState.value
//            var count = 1
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
//                if( viewModel.gameState.value.players[1].availableMoves.size != 0 ){
//                    availableMovesPerTurn[count] = Pair(
//                        availableMovesPerTurn[count]!!.first + viewModel.gameState.value.players[1].availableMoves.size,
//                        availableMovesPerTurn[count]!!.second + 1
//                    )
//                    count++
//                }
//                println("Player 1 points: ${viewModel.gameState.value.players[0].points}")
                // Simulate player 2's turn
                viewModel.checkForAiTurn()
//                if( viewModel.gameState.value.players[0].availableMoves.size != 0 ){
//                    availableMovesPerTurn[count] = Pair(
//                        availableMovesPerTurn[count]!!.first + viewModel.gameState.value.players[0].availableMoves.size,
//                        availableMovesPerTurn[count]!!.second + 1
//                    )
//                    count++
//                }
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
//                if(playerOne.points >86) {
//                    println("Player 1 wins with ${playerOne.points} points")
//                }
                playerOneWins++
                //println("Player 1 wins with ${playerOne.points} points")
            } else if(playerOne.points < playerTwo.points) {
//                if(playerTwo.points >86) {
//                    println("Player 2 wins with ${playerTwo.points} points")
//                }
                playerTwoWins++
                //println("Player 2 wins with ${playerTwo.points} points")
//            } else if(playerOne.points >76){
//                println("It's a draw with ${playerOne.points} points each")
//            }
            assertTrue(viewModel.gameState.value.isFinished)
        }
        }
//    for ((turn, moves) in availableMovesPerTurn) {
//        println("Turn $turn: verfügbare Züge nach dem Zug ${moves.first} wurden geupdated ${moves.second} ")
//    }
        println("Player 1 wins: $playerOneWins")
        println("Player 2 wins: $playerTwoWins")
    }
}