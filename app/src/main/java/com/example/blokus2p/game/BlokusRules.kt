package com.example.blokus2p.game

class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyomino: Polyomino,
        board: GameBoard,
        selectedPosition: Pair<Int, Int>
    ): Boolean {
        //grenzt position an Ecke?
        //sind alle Felder des Teils auf dem Board?
        //sind alle felder unbesetzt?
        //sind keine angrenzenden felder in der selben Farbe?
        val boardIndexesOfPolyomino: MutableList<Int> = mutableListOf()
        polyomino.currentVariant.forEach{ (x,y)->
            boardIndexesOfPolyomino.add(y*board.boardSize+x)
        }
        val selectedIndex = selectedPosition.second *  board.boardSize + selectedPosition.first
        if (
            player.points != 0 &&
            !((selectedIndex >= 14 && selectedIndex % 14 != 0
                    && selectedIndex - 15 in 0 until 196
                    && board.boardGrid[selectedIndex - 15] == player.id) ||
                    (selectedIndex >= 14 && (selectedIndex - 13) % 14 != 0
                            && selectedIndex - 13 in 0 until 196
                            && board.boardGrid[selectedIndex - 13] == player.id) ||
                    (selectedIndex <= 181 && selectedIndex % 14 != 0
                            && selectedIndex + 13 in 0 until 196
                            && board.boardGrid[selectedIndex + 13] == player.id) ||
                    (selectedIndex <= 181 && (selectedIndex - 13) % 14 != 0
                            && selectedIndex + 15 in 0 until 196
                            && board.boardGrid[selectedIndex + 15] == player.id))
        ){return false}


        for (index in boardIndexesOfPolyomino) {
            if (board.boardGrid[index] != 0) {
                return false
            }
        }


        val indexesAroundPolyomino: MutableList<Int> = mutableListOf()
        for (index in boardIndexesOfPolyomino) {
            if (index >= 14) indexesAroundPolyomino.add(index - 14)
            if (index.mod(14) != 0) indexesAroundPolyomino.add(index - 1)
            if ((index - 13).mod(14) != 0) indexesAroundPolyomino.add(index + 1)
            if (index <= 195) indexesAroundPolyomino.add(index + 14)
            indexesAroundPolyomino.forEach {
                if (it < 0 || it >= 196) {
                    return@forEach
                } else if (board.boardGrid[it] == player.id) {
                    return false
                }
            }
        }

        return true
    }
}