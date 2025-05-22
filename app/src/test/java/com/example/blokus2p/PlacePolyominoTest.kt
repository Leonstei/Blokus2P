package com.example.blokus2p

import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.Player
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.helper.isBitSet
import com.example.blokus2p.helper.mapCellsToBoardIndexes
import com.example.blokus2p.helper.setBit
import com.example.blokus2p.model.PolyominoNames
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.time.measureTime

class PlacePolyominoTest {
    val player = Player(1, "Player 1", availableEdges = setOf(130))
    val selectedPosition = 130
    val rules = BlokusRules()
    val gameEngine = GameEngine()
    val einnerPolyomino = Polyomino(PolyominoNames.EINS, 1, false, cells=listOf(0))
    @Test
    fun placePolyomino() {
        val takenTime = measureTime {
            repeat(1000) {
                val board = BlokusBoard()
                val result = gameEngine.place(
                    player,
                    mapCellsToBoardIndexes(einnerPolyomino, selectedPosition),
                    selectedPosition,
                    board,
                    rules
                )
                // Assert the result
                setBit(board.boardGrid, selectedPosition)
                assertTrue(result != null)
                assert(
                    isBitSet(result!!.boardGrid, selectedPosition) == isBitSet(
                        board.boardGrid,
                        selectedPosition
                    )
                )
            }
        }
        println("Time taken: $takenTime")
    }
}