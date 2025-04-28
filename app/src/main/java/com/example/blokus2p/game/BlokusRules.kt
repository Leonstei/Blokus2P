package com.example.blokus2p.game

import android.util.Log
import kotlin.time.measureTime

class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyominoCells: List<Pair<Int,Int>>,
        board: GameBoard,
        selectedPosition: Pair<Int, Int>
    ): Boolean {

        //sind alle Felder des Teils auf dem Board?
        //grenzt position an Ecke?
        //sind alle felder unbesetzt?
        //sind keine angrenzenden felder in der selben Farbe?

        val selectedIndex = selectedPosition.second * board.boardSize + selectedPosition.first
        val boardIndexesOfPolyomino: MutableList<Int> = mutableListOf()
        polyominoCells.forEach { (x, y) ->
            if(x+selectedPosition.first >13 || x+selectedPosition.first <0)
                return false
            boardIndexesOfPolyomino.add(y * board.boardSize + x + selectedIndex)
        }

        var bordIndexIsInEdges:Boolean = false
        for (index in boardIndexesOfPolyomino) {
            if (index !in 0 until 196) return false
            if (board.boardGrid[index] != 0) {
                return false
            }
            if(index in player.availableEdges) bordIndexIsInEdges = true
        }

        if(!bordIndexIsInEdges)
            return false


//        if (
//            player.points != 0 &&
//            !((selectedIndex >= 14 && selectedIndex % 14 != 0 && selectedIndex - 15 in 0 until 196 && board.boardGrid[selectedIndex - 15] == player.id) ||
//                    (selectedIndex >= 14 && (selectedIndex - 13) % 14 != 0 && selectedIndex - 13 in 0 until 196 && board.boardGrid[selectedIndex - 13] == player.id) ||
//                    (selectedIndex <= 181 && selectedIndex % 14 != 0 && selectedIndex + 13 in 0 until 196 && board.boardGrid[selectedIndex + 13] == player.id) ||
//                    (selectedIndex <= 181 && (selectedIndex - 13) % 14 != 0 && selectedIndex + 15 in 0 until 196 && board.boardGrid[selectedIndex + 15] == player.id))
//        ) {
//            return false
//        }


        val indexesAroundPolyomino: MutableSet<Int> = mutableSetOf()
        for (index in boardIndexesOfPolyomino) {
            if (index >= 14) indexesAroundPolyomino.add(index - 14)
            if (index.mod(14) != 0) indexesAroundPolyomino.add(index - 1)
            if ((index - 13).mod(14) != 0) indexesAroundPolyomino.add(index + 1)
            if (index <= 195) indexesAroundPolyomino.add(index + 14)
        }
        indexesAroundPolyomino.forEach {
            if (it < 0 || it >= 196) {
                return@forEach
            } else if (board.boardGrid[it] == player.id) {
                return false
            }
        }

        return true
    }
}