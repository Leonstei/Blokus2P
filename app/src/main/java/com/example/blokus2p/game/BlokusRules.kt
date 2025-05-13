package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.helper.isBitSet
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
            if (x + selectedPosition.first > 13 || x + selectedPosition.first < 0)
                return false
            boardIndexesOfPolyomino.add(y * board.boardSize + x + selectedIndex)
        }

        var bordIndexIsInEdges = false
        for (index in boardIndexesOfPolyomino) {
            if (index !in 0 until 196) return false
            if (board.boardGrid[index] != 0) {
                return false
            }
            if (index in player.availableEdges) bordIndexIsInEdges = true
        }

        if (!bordIndexIsInEdges)
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

    override fun isValidPlacement(
        player: Player,
        polyominoCells: List<Int>,
        board: BlokusBoard2,
        selectedPosition: Int
    ): Boolean {

        val boardIndexesOfPolyomino: MutableList<Int> = mutableListOf()
//        polyominoCells.forEach { index ->
//            boardIndexesOfPolyomino.add(index + selectedPosition)
//        }

        val timeTaken3 = measureTime {
            val boardIndexLeft: MutableList<Int> = mutableListOf()
            val boardIndexRight: MutableList<Int> = mutableListOf()
            polyominoCells.forEach { index ->
                if (index % 14 == 0) boardIndexLeft.add(index)
                if (index % 14 == 13) boardIndexRight.add(index)
            }
            if(boardIndexLeft.isNotEmpty() && boardIndexRight.isNotEmpty()) {
                return false
            }
        }
        Log.d("AppViewModel", "Time taken check out of edge: $timeTaken3 ")

        var bordIndexIsInEdges = false
        for (index in polyominoCells) {
            if (index !in 0 until 196) return false
            if (isBitSet(board.boardGrid,index)) {
                return false
            }
            if (index in player.availableEdges) bordIndexIsInEdges = true
        }


        if (!bordIndexIsInEdges)
            return false

        val timeTaken = measureTime {
            val indexesAroundPolyomino: MutableSet<Int> = mutableSetOf()
            polyominoCells.forEach {
                if (it >= 14) indexesAroundPolyomino.add(it - 14)
                if (it.mod(14) != 0) indexesAroundPolyomino.add(it - 1)
                if ((it - 13).mod(14) != 0) indexesAroundPolyomino.add(it + 1)
                if (it <= 195) indexesAroundPolyomino.add(it + 14)
            }
            indexesAroundPolyomino.forEach {
                if (it < 0 || it >= 196) {
                    return@forEach
                } else if (isBitSet(player.bitBoard, it)) {
                    return false
                }
            }
        }
        Log.d("AppViewModel", "Time taken checkindexesAroundPolyomino: $timeTaken ")

        val indexesAroundPolyomino: MutableSet<Int> = mutableSetOf()
        polyominoCells.forEach {
            if (it >= 14) indexesAroundPolyomino.add(it - 14)
            if (it.mod(14) != 0) indexesAroundPolyomino.add(it - 1)
            if ((it - 13).mod(14) != 0) indexesAroundPolyomino.add(it + 1)
            if (it <= 195) indexesAroundPolyomino.add(it + 14)
        }
        indexesAroundPolyomino.forEach {
            if (it < 0 || it >= 196) {
                return@forEach
            } else if (isBitSet(player.bitBoard, it)) {
                return false
            }
        }

        val timeTaken2 = measureTime {
            polyominoCells.forEach {
                if (it >= 14  && isBitSet(player.bitBoard, it - 14)) return false
                if (it.mod(14) != 0 && it - 1 >= 0 && isBitSet(player.bitBoard, it - 1)) return false
                if ((it - 13).mod(14) != 0 && it + 1 < 196 && isBitSet(player.bitBoard, it + 1)) return false
                if (it < 182 && isBitSet(player.bitBoard, it + 14)) return false
            }
        }
        Log.d("AppViewModel", "Time taken checkindexesAroundPolyomino2: $timeTaken2 ")


        return true
    }
}