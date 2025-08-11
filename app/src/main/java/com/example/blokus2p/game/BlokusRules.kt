package com.example.blokus2p.game

import com.example.blokus2p.helper.FIRST_INDEX_OFBOARD
import com.example.blokus2p.helper.FIRST_INDEX_ONBOARD
import com.example.blokus2p.helper.ROW_SIZE
import com.example.blokus2p.helper.isBitSet
import com.example.blokus2p.model.Player
import com.example.blokus2p.model.SmalBoard
import com.example.blokus2p.model.SmalPlayer
import kotlin.time.measureTime


class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyominoCells: List<Int>,
        board: GameBoard
    ): Boolean {
        var bordIndexIsInEdges = false
        for (index in polyominoCells) {
            if (index !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD)
                return false
            if (isBitSet(board.boardGrid,index)) {
                return false
            }
            if (index in player.availableEdges) bordIndexIsInEdges = true
        }
        if (!bordIndexIsInEdges)
            return false

        polyominoCells.forEach {
            if (it >= 33  && isBitSet(player.bitBoard, it - ROW_SIZE))
                return false
            if (it.mod(ROW_SIZE) != 1 && it - 1 >= FIRST_INDEX_ONBOARD && isBitSet(player.bitBoard, it - 1))
                return false
            if (it.mod(ROW_SIZE) != 14 && it + 1 < FIRST_INDEX_OFBOARD && isBitSet(player.bitBoard, it + 1))
                return false
            if (it < 225 && isBitSet(player.bitBoard, it + ROW_SIZE))
                return false
        }

        return true
    }

    override fun isValidPlacementSmal(
        player: SmalPlayer,
        polyominoCells: List<Int>,
        board: SmalBoard
    ): Boolean {
        var bordIndexIsInEdges = false
        for (index in polyominoCells) {
            if (index !in FIRST_INDEX_ONBOARD until FIRST_INDEX_OFBOARD)
                return false
            if (isBitSet(board.boardGrid,index)) {
                return false
            }
            if (index in player.availableEdges) bordIndexIsInEdges = true
        }
        if (!bordIndexIsInEdges)
            return false



        polyominoCells.forEach {
            if (it >= 33  && isBitSet(player.bitBoard, it - ROW_SIZE))
                return false
            if (it.mod(ROW_SIZE) != 1 && it - 1 >= FIRST_INDEX_ONBOARD && isBitSet(player.bitBoard, it - 1))
                return false
            if (it.mod(ROW_SIZE) != 14 && it + 1 < FIRST_INDEX_OFBOARD && isBitSet(player.bitBoard, it + 1))
                return false
            if (it < 225 && isBitSet(player.bitBoard, it + ROW_SIZE))
                return false
        }
        return true
    }

}