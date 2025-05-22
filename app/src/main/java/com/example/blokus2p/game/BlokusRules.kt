package com.example.blokus2p.game

//import android.util.Log
import com.example.blokus2p.helper.isBitSet
import kotlin.time.measureTime


class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyominoCells: List<Int>,
        board: BlokusBoard,
        selectedPosition: Int
    ): Boolean {
        val boardIndexLeft: MutableList<Int> = mutableListOf()
        val boardIndexRight: MutableList<Int> = mutableListOf()
        polyominoCells.forEach { index ->
            if (index % 14 == 0) boardIndexLeft.add(index)
            if (index % 14 == 13) boardIndexRight.add(index)
        }
        if(boardIndexLeft.isNotEmpty() && boardIndexRight.isNotEmpty()) {
            return false
        }
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

        polyominoCells.forEach {
            if (it >= 14  && isBitSet(player.bitBoard, it - 14)) return false
            if (it.mod(14) != 0 && it - 1 >= 0 && isBitSet(player.bitBoard, it - 1)) return false
            if ((it - 13).mod(14) != 0 && it + 1 < 196 && isBitSet(player.bitBoard, it + 1)) return false
            if (it < 182 && isBitSet(player.bitBoard, it + 14)) return false
        }
//        val timeTaken = measureTime {
//            val boardIndexLeft: MutableList<Int> = mutableListOf()
//            val boardIndexRight: MutableList<Int> = mutableListOf()
//            polyominoCells.forEach { index ->
//                if (index % 14 == 0) boardIndexLeft.add(index)
//                if (index % 14 == 13) boardIndexRight.add(index)
//            }
//            if(boardIndexLeft.isNotEmpty() && boardIndexRight.isNotEmpty()) {
//                return false
//            }
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