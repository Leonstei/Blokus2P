package com.example.blokus2p

import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.PlayerType
import com.example.blokus2p.viewModel.AppViewModel
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.time.measureTime

class RandomAiVSMinmaxAi {
    @Test
    fun testRandomAiVsMinmaxAi() {
        var playerOneWins = 0
        var playerTwoWins = 0
        val takenTime = measureTime {
            repeat(50) {
                val viewModel: AppViewModel = AppViewModel()
                var count = 0
                val gameState = viewModel.gameState.value
                viewModel.onEvent(
                    GameEvent.ChangePlayerSettings(
                        gameState.players[0].name, gameState.players[1].name,
                        gameState.players[0].color, gameState.players[1].color,
                        PlayerType.MinimaxAI, PlayerType.RandomAI
                    )
                )

                while (viewModel.gameState.value.isFinished == false && count < 21) {
                    // Simulate player 1's turn
                    viewModel.checkForAiTurn()
//                println("Player 1 points: ${viewModel.gameState.value.players[0].points}")
                    // Simulate player 2's turn
                    viewModel.checkForAiTurn()
//                println("Player 2 points: ${viewModel.gameState.value.players[1].points}")
                    count++
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
                if (playerOne.points > playerTwo.points) {
                    playerOneWins++
                    println("Player 1 wins with ${playerOne.points} points against ${playerTwo.points} points")
                } else if (playerOne.points < playerTwo.points) {
                    playerTwoWins++
                    println("Player 2 wins with ${playerTwo.points} points against ${playerOne.points} points")
                } else {
                    println("It's a draw with ${playerOne.points} points each")
                }
                assertTrue(newGameState.isFinished)
            }
            println("Player 1 wins: $playerOneWins")
            println("Player 2 wins: $playerTwoWins")
        }
        println("Time taken: $takenTime")
    }
}