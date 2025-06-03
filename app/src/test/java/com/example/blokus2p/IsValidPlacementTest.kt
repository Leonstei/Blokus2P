package com.example.blokus2p

import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.Player
import com.example.blokus2p.helper.setBit
import junit.framework.TestCase.assertFalse
import org.junit.Test

class IsValidPlacementTest {


    @Test
    fun allCellsOnBoard() {
        val player = Player(1, "Player 1", availableEdges = setOf(15))
        val board = BlokusBoard()
        val selectedPosition = 15
        val rules = BlokusRules()


        val result = rules.isValidPlacement(
            player,
            listOf(1,14,29,15,16),
            board,
        )

        // Assert the result
        assert(result)
    }

    @Test
    fun allCellsOffBoard() {
        val player = Player(1, "Player 1", availableEdges = setOf(15))
        val board = BlokusBoard()
        val selectedPosition = -15
        val rules = BlokusRules()
        val result = rules.isValidPlacement(
            player,
            listOf(-1,-14,-29,-15,196),
            board,
        )

        // Assert the result
        assertFalse(result)
    }
    @Test
    fun cellsGoOverLeftAndRightEdges() {
        val player = Player(1, "Player 1", availableEdges = setOf(14,55))
        val board = BlokusBoard()
        val selectedPositionLeft = 14
        val selectedPositionRight = 55
        val rules = BlokusRules()
        val resultLeft = rules.isValidPlacement(
            player,
            listOf(0,13,14,28,15),
            board,
        )
        val resultRight = rules.isValidPlacement(
            player,
            listOf(55,68,69,83,70),
            board,
        )

        // Assert the result
        assertFalse(resultLeft)
        assertFalse(resultRight)

    }
    @Test
    fun noCellOnAvailableEdges() {
        val player = Player(1, "Player 1", availableEdges = setOf(14,55))
        val board = BlokusBoard()
        val selectedPositionTop = 0
        val rules = BlokusRules()
        val resultTop = rules.isValidPlacement(
            player,
            listOf(16,30,31),
            board,
        )
        // Assert the result
        assertFalse(resultTop)
    }
    //cellen sind bereits von einem Spieler belegt
    @Test
    fun cellsOnoccupiedPlaces(){
        val player = Player(1, "Player 1", availableEdges = setOf(14,55))
        val board = BlokusBoard()
        val selectedPositionTop = 0
        val rules = BlokusRules()
        setBit(board.boardGrid, 14)
        val resultTop = rules.isValidPlacement(
            player,
            listOf(0,14,15),
            board,
        )
        // Assert the result
        assertFalse(resultTop)
    }

    //cellen drumherum sind bereits von einem Spieler selbst belegt
    @Test
    fun cellsAroundOccupiedPlaces(){
        val player = Player(1, "Player 1", availableEdges = setOf(14,15,17,20))
        val board = BlokusBoard()
        val selectedPositionTop = 0
        val rules = BlokusRules()
        setBit(player.bitBoard, 0)
        setBit(player.bitBoard, 16)
        setBit(player.bitBoard, 31)
        setBit(player.bitBoard, 19)
        val resultTop = rules.isValidPlacement(
            player,
            listOf(14),
            board,
        )
        val resultRight = rules.isValidPlacement(
            player,
            listOf(15),
            board,
        )
        val resultDown = rules.isValidPlacement(
            player,
            listOf(17),
            board,
        )
        val resultLeft = rules.isValidPlacement(
            player,
            listOf(20),
            board,
        )
        print("resultTop: $resultTop, resultRight: $resultRight, resultDown: $resultDown, resultLeft: $resultLeft")
        // Assert the result
        assertFalse(resultTop)
        assertFalse(resultRight)
        assertFalse(resultDown)
        assertFalse(resultLeft)
    }

}