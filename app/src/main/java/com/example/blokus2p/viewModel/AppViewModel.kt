package com.example.blokus2p.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.Events.AppEvent
import com.example.blokus2p.model.Events.GameEvent
import com.example.blokus2p.game.Polyomino
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
    private val rules = BlokusRules()

    init {
        _gameSate.update {
            it.copy(
                activPlayer_id = 1,
                activPlayer = Player(1, "Player 1", Color.Blue, 0),
                playerTwo = Player(2, "Player 2", Color.Magenta, 0),
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
                        boardGrid = Array(_gameSate.value.board.boardSize * _gameSate.value.board.boardSize) {0},
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
        if (_gameSate.value.selectedPolyomino.name == "") return
        setSelectedCellFirst()
        val newBoard = GameEngine(_gameSate.value.board, rules).place(
            _gameSate.value.activPlayer,
            _gameSate.value.selectedPolyomino,
            col,
            row
        )
        if (newBoard != null){
            _gameSate.update { state ->
                state.copy(
                    board = newBoard,
                    boardGrid = newBoard.boardGrid,
                    activPlayer = state.activPlayer.copy(
                        points = state.activPlayer.points + state.selectedPolyomino.points,
                        placedPolyomino = state.selectedPolyomino.copy(cells = findPositions(col, row))
                    )
                )
            }
        } else {
            Log.d("AppViewModel", "Polyomino konnte nicht platziert werden")
        }
    }

    private fun setSelectedCellFirst(){
        if(_gameSate.value.selectedPolyomino.selectedCell == Pair(0,0))
            return
        _gameSate.update { state->
            state.copy(
                selectedPolyomino = state.selectedPolyomino.copy(
                    cells = state.selectedPolyomino.currentVariant.map {
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


    private fun undoPlace(){
        val newBoard =GameEngine(_gameSate.value.board,rules).undoplace()
        if (newBoard != null){
            _gameSate.update { state ->
                state.copy(
                    board = newBoard,
                    boardGrid = newBoard.boardGrid,
                    activPlayer = state.activPlayer.copy(
                        points = state.activPlayer.points - state.selectedPolyomino.points)
                )
            }
        }else{
        }
//        val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
//        _gameSate.value.activPlayer.placedPolyomino.cells.forEach { cell ->
//            positionen.add(cell)
//        }
//        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
//        _gameSate.update { state ->
//            positionen.forEach { (x, y) ->
//                if (x in 0 until state.board.boardSize && y in 0 until state.board.boardSize) {
//                    updatedBoardGrid[y * _gameSate.value.board.boardSize + x] =0
//                }
//            }
//
//            state.copy(
//                boardGrid = updatedBoardGrid,
//                activPlayer = state.activPlayer.copy(
//                    points = state.activPlayer.points - state.activPlayer.placedPolyomino.points,
//                    placedPolyomino = Polyomino(),
//                    polyominoIsPlaced = false
//                )
//            )
//        }
    }

    private fun findPositions(col: Int,row: Int): MutableList<Pair<Int, Int>> {
        val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
        _gameSate.value.selectedPolyomino.cells.forEach { cell ->
            if (cell.first + col in 0 until _gameSate.value.board.boardSize&&
                cell.second + row in 0 until _gameSate.value.board.boardSize
            ) {
                if(cell == Pair(0,0)) {
                    selectedPosition = Pair(cell.first + col, cell.second + row)
                }
                positionen.add(Pair(cell.first + col, cell.second + row))
            }
        }
        return positionen
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
        Log.d("AppViewModel", "activPlayer poly : ${_gameSate.value.activPlayer.polyominos}")
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
}
