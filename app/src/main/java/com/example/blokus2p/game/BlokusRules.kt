package com.example.blokus2p.game

import android.util.Log
import com.example.blokus2p.helper.isBitSet
import kotlin.time.measureTime


class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyominoCells: List<Int>,
        board: BlokusBoard,
        selectedPosition: Int
    ): Boolean {
        var bordIndexIsInEdges = false
        for (index in polyominoCells) {
            if (index !in 0 until 196)
                return false
            if (isBitSet(board.boardGrid,index)) {
                return false
            }
            if (index in player.availableEdges) bordIndexIsInEdges = true
        }
        if (!bordIndexIsInEdges)
            return false

        var boardIndexLeft = false
        var boardIndexRight = false
        polyominoCells.forEach { index ->
            if (index % 14 == 0) boardIndexLeft = true
            if (index % 14 == 13) boardIndexRight = true
        }
        if (boardIndexLeft && boardIndexRight) {
            return false
        }

        polyominoCells.forEach {
            if (it >= 14  && isBitSet(player.bitBoard, it - 14))
                return false
            if (it.mod(14) != 0 && it - 1 >= 0 && isBitSet(player.bitBoard, it - 1))
                return false
            if ((it - 13).mod(14) != 0 && it + 1 < 196 && isBitSet(player.bitBoard, it + 1))
                return false
            if (it < 182 && isBitSet(player.bitBoard, it + 14))
                return false
        }


//        val timeTaken3 = measureTime {
//            var boardIndexLeft = false
//            var boardIndexRight = false
//            polyominoCells.forEach { index ->
//                if (index % 14 == 0 ) boardIndexLeft = true
//                if (index % 14 == 13 ) boardIndexRight = true
//            }
//            if(boardIndexLeft && boardIndexRight) {
//                return false
//            }
//        }
//        println("isValidPlacement my version 2: $timeTaken3")
        //Log.d("AppViewModel", "isValidPlacement my version 2: $timeTaken3")
//        val timeTaken2 = measureTime {
//            val boardIndexLeft2: MutableList<Int> = mutableListOf()
//            val boardIndexRight2: MutableList<Int> = mutableListOf()
//            polyominoCells.forEach { index ->
//                if (index % 14 == 0) boardIndexLeft2.add(index)
//                if (index % 14 == 13) boardIndexRight2.add(index)
//            }
//            if(boardIndexLeft2.isNotEmpty() && boardIndexRight2.isNotEmpty()) {
//                return false
//            }
//        }
        //println("isValidPlacement my version: $timeTaken2")
        //Log.d("AppViewModel", "isValidPlacement my version: $timeTaken2")



//            var bordIndexIsInEdges = false
//            for (index in polyominoCells) {
//                if (index !in 0 until 196) return false
//                if (isBitSet(board.boardGrid,index)) {
//                    return false
//                }
//                if (index in player.availableEdges) bordIndexIsInEdges = true
//            }
//            if (!bordIndexIsInEdges)
//                return false
//
//            polyominoCells.forEach {
//                if (it >= 14  && isBitSet(player.bitBoard, it - 14)) return false
//                if (it.mod(14) != 0 && it - 1 >= 0 && isBitSet(player.bitBoard, it - 1)) return false
//                if ((it - 13).mod(14) != 0 && it + 1 < 196 && isBitSet(player.bitBoard, it + 1)) return false
//                if (it < 182 && isBitSet(player.bitBoard, it + 14)) return false
//            }
//        }
//        Log.d("AppViewModel", "isValidPlacement: $timeTaken")
        return true
    }

}