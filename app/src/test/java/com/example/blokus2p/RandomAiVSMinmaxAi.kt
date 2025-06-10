package com.example.blokus2p

import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.Player
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.model.Move
import com.example.blokus2p.model.PlayerType
import com.example.blokus2p.model.PolyominoNames
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
            repeat(1) {
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

    @Test
    fun humanvsHuman() {
        val viewModel: AppViewModel = AppViewModel()
        viewModel.onEvent(
            GameEvent.ChangePlayerSettings(
                "Player 1", "Player 2",
                viewModel.gameState.value.players[0].color, viewModel.gameState.value.players[1].color,
                PlayerType.Human, PlayerType.Human))
        val moves = listOf(
            Move(Polyomino(PolyominoNames.FÜNF_L, 5, false, cells=listOf(0, 14, 28, 42, 43)),listOf(88, 102, 116, 130, 131),130),
            Move(Polyomino(
                PolyominoNames.FÜNF_L,
                5,
                false,
                cells=listOf(0, 14, 28, 42, 43)
            ),listOf(65, 79, 93, 107, 106),65),
            Move(Polyomino(
                PolyominoNames.FÜNF,
                5,
                false,
                cells=listOf(0, 14, 28, 42, 56)
            ), listOf(118, 119, 120, 121, 122),118),
            Move(        Polyomino(
                PolyominoNames.FÜNF_7,
                5,
                false,
                cells=listOf(0, 14, 15, 29, 16)
            ), listOf(91, 90, 104, 89, 75),91),
            Move(        Polyomino(
                PolyominoNames.DREI,
                3,
                false,
                cells=listOf(0, 14, 28)
            ), listOf(109, 110, 111),109),
            Move(        Polyomino(
                PolyominoNames.FÜNF_Z,
                5,
                false,
                cells=listOf(0, 14, 15, 16, 30)
            ), listOf(32, 46, 47, 48, 62),62),
        )
        moves.forEach {
            val gameState = viewModel.gameState.value
            viewModel.selectPolyomino(
                it.polyomino,
                it.position
            )
            val newBoard = GameEngine().placeAiMove(
                gameState.activPlayer,
                it.polyomino,
                it.position,
                gameState.board, viewModel.rules, it.orientation
            )
            viewModel.updateBoard(newBoard)
            viewModel.updatePolyominosOfActivPlayer(gameState.activPlayer_id)
            viewModel.updateAvailableEdgesActivPlayer()
            viewModel.updateAvailableMoves()
            viewModel.nextPlayer(gameState.activPlayer_id)
        }
    }
}
