package com.example.blokus2p.viewModel

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.blokus2p.model.Events.AppEvent
import com.example.blokus2p.model.Events.GameEvent
import com.example.blokus2p.model.Events.Polyomino
import com.example.blokus2p.model.Events.PolyominoEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel() : ViewModel() {
    private val _gameSate = MutableStateFlow(GameState())
    val timerState: StateFlow<GameState> = _gameSate.asStateFlow()

    private val _polyominoState = MutableStateFlow(PolyominoSate())
    val polyominoState: StateFlow<PolyominoSate> = _polyominoState.asStateFlow()

    private var selectedPosition: Pair<Int, Int>? = null

    init {
        _gameSate.update {
            it.copy(
                activPlayer_id = 1,
                activPlayer = Player(1, "Player 1", androidx.compose.ui.graphics.Color.Blue, 0),
                playerTwo = Player(2, "Player 2", androidx.compose.ui.graphics.Color.Magenta, 0),
                playerOneColor = androidx.compose.ui.graphics.Color.Blue,
                playerTwoColor = androidx.compose.ui.graphics.Color.Magenta,
            )
        }
    }

    fun onEvent(event: AppEvent) {
        when (event) {
            is GameEvent -> handleGameEvent(event)
            is PolyominoEvent -> handlePolyominoEvent(event)
        }
    }

    fun handlePolyominoEvent(event: PolyominoEvent) {
        when (event) {
            is PolyominoEvent.PolyominoSelected -> {
                _gameSate.update { state ->
                    state.copy(
                        selectedPolyomino = event.polyomino.copy(
                            selectedCell = event.selectedCell
                        ),
                        activPlayer = _gameSate.value.activPlayer.copy(
                            polyominos = _gameSate.value.activPlayer.polyominos.map {
                                if (it.name == event.polyomino.name) {
                                    it.copy(isSelected = true,
                                        selectedCell = event.selectedCell
                                    )
                                } else {
                                    it.copy(isSelected = false)
                                }
                            }
                        )
                    )
                }

            }

            is PolyominoEvent.PolyominoRotate -> {
                flippPolyomino()
            }

            is PolyominoEvent.PolyominoRotateClockwise -> {
                rotateRight()
            }
            is PolyominoEvent.PolyominoRotateCounterClockwise -> {
                rotateLeft()
            }
        }
    }

    fun handleGameEvent(event: GameEvent) {
        when (event) {
            is GameEvent.GameStarted -> {
            }

            is GameEvent.GameEnded -> {
            }
            is GameEvent.GameRestart -> {
                _gameSate.update {
                    it.copy(
                        activPlayer_id = 1,
                        isFinished = false,
                        activPlayer = Player(1, event.nameActivPlayer, event.colorActivPlayer, 0),
                        playerTwo = Player(2, event.namePlayerTwo, event.colorPlayerTwo, 0),
                        playerOneColor = event.colorActivPlayer,
                        playerTwoColor = event.colorPlayerTwo,
                        boardGrid = Array(_gameSate.value.gridSize * _gameSate.value.gridSize) {0},
                        selectedPolyomino = Polyomino(),
                    )
                }

            }

            is GameEvent.NextPlayer -> {
                nextPlayer(event.player)
            }

            is GameEvent.PlacePolyomino -> {
                placePolyomino(event.col, event.row)
            }
            is GameEvent.UndoPlace -> {
                undoPlace()
            }
            is GameEvent.ChangePlayerSettings -> {
                _gameSate.update {
                    it.copy(
                        activPlayer = it.activPlayer.copy(
                            name = event.nameActivPlayer,
                            color = event.colorActivPlayer
                        ),
                        playerTwo = it.playerTwo.copy(
                            name = event.namePlayerTwo,
                            color = event.colorPlayerTwo
                        ),
                        playerOneColor = event.colorActivPlayer,
                        playerTwoColor = event.colorPlayerTwo
                    )
                }
            }
        }
    }

    private fun placePolyomino(col: Int, row: Int) {
        if(_gameSate.value.activPlayer.polyominoIsPlaced) return
        if (_gameSate.value.selectedPolyomino.name == "") return
        if(_gameSate.value.selectedPolyomino.name == _gameSate.value.activPlayer.placedPolyomino.name) return
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        setSelectedCellFirst()
        val positionen = findValidPosition(col, row, 1)
        if (positionen.isEmpty()) {
            Log.d("AppViewModel", "keine gültige combination gefunden")
//            for (i in 1 until _gameSate.value.selectedPolyomino.cells.size) {
//                val nextRotationPoint =
//                    _gameSate.value.selectedPolyomino.cells[i].copy()
//                _gameSate.update { state ->
//                    state.copy(
//                        selectedPolyomino = state.selectedPolyomino.copy(
//                            cells = state.selectedPolyomino.cells.map { cell ->
//                                Pair(
//                                    cell.first - nextRotationPoint.first,
//                                    cell.second - nextRotationPoint.second
//                                )
//                            }
//                        )
//                    )
//                }
//            }
        } else {
            Log.d("AppviewModel", "positonen: $positionen")
            _gameSate.update { state ->
                positionen.forEach { (x, y) ->
                    if (x in 0 until state.gridSize && y in 0 until state.gridSize) {
                        updatedBoardGrid[y * state.gridSize + x] =
                            _gameSate.value.activPlayer_id
                    }
                }
                state.copy(
                    boardGrid = updatedBoardGrid,
                    activPlayer = state.activPlayer.copy(
                        points = state.activPlayer.points + state.selectedPolyomino.points,
                        placedPolyomino = state.selectedPolyomino.copy(cells = positionen)
                    )
                )
            }
        }
    }
    private fun setSelectedCellFirst(){
        if(_gameSate.value.selectedPolyomino.selectedCell == Pair(0,0)) return
        _gameSate.update { state->
            state.copy(
                selectedPolyomino = state.selectedPolyomino.copy(
                    cells = state.selectedPolyomino.cells.map {
                        cell->
                        Pair(
                            cell.first - _gameSate.value.selectedPolyomino.selectedCell.first
                            ,cell.second - _gameSate.value.selectedPolyomino.selectedCell.second
                        )
                    }
                )
            )
        }
    }

    private fun findValidPosition(col: Int, row: Int, count: Int): MutableList<Pair<Int, Int>> {
        var positionen: MutableList<Pair<Int, Int>> = mutableListOf()
        if (count == 1 ) {
            positionen = findPositions(col, row)
        } else if (count.mod(5) != 0) {
            rotateRight()
            positionen = findPositions(col, row)
        } else {
            flippPolyomino()
            positionen = findPositions(col, row)
        }


        if (checkValidPosition(positionen)) {
            _gameSate.update { state ->
                state.copy(
                    activPlayer = state.activPlayer.copy(
                        polyominoIsPlaced = true
                    )
                )
            }
            //var mediaPlayer = MediaPlayer.create(context, R.raw.sound_file_1)
            //mediaPlayer.start()
            return positionen
        } else if (count < 1) {
            return findValidPosition(col, row, count + 1)
        } else {
            return mutableListOf()
        }
    }


    private fun checkValidPosition(positionen: MutableList<Pair<Int, Int>>): Boolean {
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        val points = _gameSate.value.selectedPolyomino.points
        val indexOfSelectedCell = selectedPosition?.second!! * _gameSate.value.gridSize + selectedPosition?.first!!
        if (positionen.size == 0) {
            return false
        }
        if (positionen.size != points) {
            return false
        }
//        if (!_gameSate.value.activPlayer.edges.isEmpty() &&
//            _gameSate.value.activPlayer_id == _gameSate.value.activPlayer.id &&
//            !_gameSate.value.activPlayer.edges.contains(selectedPosition?.second!! * _gameSate.value.gridSize + selectedPosition?.first!!)
//        ) {
//            return false
//        }

        if(
            _gameSate.value.activPlayer.points != 0 &&
            !((indexOfSelectedCell >= 14 && indexOfSelectedCell % 14 != 0
                    && indexOfSelectedCell -15 in 0 until 196
                    && updatedBoardGrid[indexOfSelectedCell-15]==_gameSate.value.activPlayer_id) ||
            (indexOfSelectedCell >= 14 && (indexOfSelectedCell - 13) % 14 != 0
                    && indexOfSelectedCell - 13 in 0 until 196
                    && updatedBoardGrid[indexOfSelectedCell-13]==_gameSate.value.activPlayer_id)||
            (indexOfSelectedCell <= 181 && indexOfSelectedCell % 14 != 0
                    && indexOfSelectedCell + 13 in 0 until 196
                    && updatedBoardGrid[indexOfSelectedCell +13]==_gameSate.value.activPlayer_id)||
            (indexOfSelectedCell <= 181 && (indexOfSelectedCell - 13) % 14 != 0
                    && indexOfSelectedCell + 15 in 0 until 196
                    && updatedBoardGrid[indexOfSelectedCell+15]==_gameSate.value.activPlayer_id))
        ){
            return false
        }

        for (pos in positionen) {
            val index = pos.second * _gameSate.value.gridSize + pos.first
            if (updatedBoardGrid[index] != 0) {
                return false
            }
        }
        return checkIfSameCollorArround(positionen)
    }

    fun checkIfSameCollorArround(positionen: MutableList<Pair<Int, Int>>): Boolean {
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        val indexesAroundPolyomino = mutableListOf<Int>()

        val activPlayer = _gameSate.value.activPlayer_id

        for (pos in positionen) {
            val index = pos.second * _gameSate.value.gridSize + pos.first
            if (index >= 14) indexesAroundPolyomino.add(index - 14)
            if (index.mod(14) != 0) indexesAroundPolyomino.add(index - 1)
            if ((index - 13).mod(14) != 0) indexesAroundPolyomino.add(index + 1)
            if (index <= 195) indexesAroundPolyomino.add(index + 14)
            indexesAroundPolyomino.forEach {
                if (it < 0 || it >= 196) {
                    return@forEach
                } else if (updatedBoardGrid[it] == activPlayer) {
                    return false
                }
            }
        }
        return true
    }


    private fun flippPolyomino() {
        val selected = _gameSate.value.selectedPolyomino
        if (selected.cells.isEmpty()) return

        val updatedPolyomino = selected.flippHorizontal()

        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    polyominos = state.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            updatedPolyomino.copy(isSelected = true)
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = updatedPolyomino.copy(isSelected = true)
            )
        }
    }

    private fun rotateLeft() {
        val selected = _gameSate.value.selectedPolyomino
        if (selected.cells.isEmpty()) return

        val updatedPolyomino = selected.rotatedLeft()

        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    polyominos = state.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            updatedPolyomino.copy(isSelected = true)
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = updatedPolyomino.copy(isSelected = true)
            )
        }
    }

    private fun rotateRight() {
        val selected = _gameSate.value.selectedPolyomino
        if (selected.cells.isEmpty()) return

        val updatedPolyomino = selected.rotatedRight()

        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    polyominos = state.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            updatedPolyomino.copy(isSelected = true)
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = updatedPolyomino.copy(isSelected = true)
            )
        }
    }


    private fun nextPlayer(player_id: Int) {
        val newPlayer = if (player_id == 1) {
            2
        } else 1
        _gameSate.update {
            it.copy(
                activPlayer = it.activPlayer.copy(
                    polyominos = it.activPlayer.polyominos.filterNot { polyomino ->
                        polyomino.name == it.activPlayer.placedPolyomino.name
                    },
                    polyominoIsPlaced = false
                ),
                selectedPolyomino = Polyomino()
            )
        }
        _gameSate.update {
            it.copy(
                activPlayer_id = newPlayer,
                activPlayer = it.playerTwo,
                playerTwo = it.activPlayer
            )
        }
    }


//    private fun positonEdges(positionen: MutableList<Pair<Int, Int>>): Set<Int> {
//        val edges: MutableSet<Int> = mutableSetOf()
//        for (pos in positionen) {
//            val index = pos.second * _gameSate.value.gridSize + pos.first
//            if (index >= 14 && index % 14 != 0 && index + 15 in 0 until 196) {
//                edges.add(index + 15)
//            }
//            if (index >= 14 && (index - 13) % 14 != 0 && index + 13 in 0 until 196) {
//                edges.add(index + 13)
//            }
//            if (index <= 195 && index % 14 != 0 && index - 15 in 0 until 196) {
//                edges.add(index - 15)
//            }
//            if (index <= 195 && (index - 13) % 14 != 0 && index - 13 in 0 until 196) {
//                edges.add(index - 13)
//            }
//        }
//        return edges
//    }

    private fun undoPlace(){
        val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
        _gameSate.value.activPlayer.placedPolyomino.cells.forEach { cell ->
            positionen.add(cell)
        }
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        _gameSate.update { state ->
            positionen.forEach { (x, y) ->
                if (x in 0 until state.gridSize && y in 0 until state.gridSize) {
                    updatedBoardGrid[y * _gameSate.value.gridSize + x] =0
                }
            }

            state.copy(
                boardGrid = updatedBoardGrid,
                activPlayer = state.activPlayer.copy(
                    points = state.activPlayer.points - state.activPlayer.placedPolyomino.points,
                    placedPolyomino = Polyomino(),
                    polyominoIsPlaced = false
                )
            )
        }
    }

    private fun findPositions(col: Int,row: Int): MutableList<Pair<Int, Int>> {
        val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
        _gameSate.value.selectedPolyomino.cells.forEach { cell ->
            if (cell.first + col in 0 until _gameSate.value.gridSize &&
                cell.second + row in 0 until _gameSate.value.gridSize
            ) {
                if(cell == Pair(0,0)) {
                    selectedPosition = Pair(cell.first + col, cell.second + row)
                }
                positionen.add(Pair(cell.first + col, cell.second + row))
            }
        }
        return positionen
    }
}
