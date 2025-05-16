package com.example.blokus2p

import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.helper.clearBit
import com.example.blokus2p.helper.isBitSet
import com.example.blokus2p.helper.setBit
import org.junit.Assert.assertThrows
import org.junit.Test

class BitOoperationsTest {

    @Test
    fun setBitOnBlokusboard2(){
        val board = BlokusBoard()
        val index = 195
        for(i in 0 until 196 step 2){
            setBit(board.boardGrid, i)
        }
        //setBit(board.boardGrid, index)
        println("boardGrid: ${board.boardGrid.joinToString(",")}")
        //assert(isBitSet(board.boardGrid, index))
        for (i in 0 until 196){
            if (isBitSet(board.boardGrid, i)){
                print(1)
            }else print(0)
        }
        for (i in 0 until 65 step 2){
            assert(isBitSet(board.boardGrid, i))
        }
    }
    @Test
    fun clearBitFromBlokusboard2(){
        val board = BlokusBoard()
        val index = 0
        for(i in 0 until 196 step 2){
            setBit(board.boardGrid, i)
        }
        clearBit(board.boardGrid, index)
        println("boardGrid: ${board.boardGrid.joinToString(",")}")
        //assert(isBitSet(board.boardGrid, index))
        for (i in 0 until 196){
            if (isBitSet(board.boardGrid, i)){
                print(1)
            }else print(0)
        }
        assert(!isBitSet(board.boardGrid, index))
    }

    @Test
    fun setBitNegativeIndex(){
        val board = BlokusBoard()
        val index = -1
        assertThrows(ArrayIndexOutOfBoundsException::class.java) {
            setBit(board.boardGrid, index)
        }

        assertThrows(ArrayIndexOutOfBoundsException::class.java) {
            assert(isBitSet(board.boardGrid, index))
        }
    }

}