package com.example.blokus2p.viewModel

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
                        selectedPolyomino = event.polyomino,
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
                rotatePolyomino()
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

            is GameEvent.NextPlayer -> {
                nextPlayer(event.player)
            }

            is GameEvent.PlacePolyomino -> {
                placePolyomino(event.col, event.row)
            }
            is GameEvent.UndoPlace -> {
                val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
                _gameSate.value.activPlayer.placedPolyomino.cells.forEach { cell ->
                    positionen.add(cell)
                }
                val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
                _gameSate.update { state ->
                    positionen.forEach { (x, y) ->
                        if (x in 0 until state.gridSize && y in 0 until state.gridSize) {
                            updatedBoardGrid[y * state.gridSize + x] =0
                        }
                    }
                    state.copy(
                        boardGrid = updatedBoardGrid,
                        activPlayer = state.activPlayer.copy(
                            points = state.activPlayer.points - state.activPlayer.placedPolyomino.points,
                            placedPolyomino = Polyomino()
                        )
                    )
                }
            }

        }
    }

    private fun placePolyomino(col: Int, row: Int) {
        if (_gameSate.value.selectedPolyomino.name == "") return
        if(_gameSate.value.selectedPolyomino.name == _gameSate.value.activPlayer.placedPolyomino.name) return
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        val positionen = findValidPosition(col, row, 1)
        if (positionen.isEmpty()) {
            Log.d("AppViewModel", "keine gültige combination gefunden")
            for (i in 1 until _gameSate.value.selectedPolyomino.cells.size) {
                val nextRotationPoint =
                    _gameSate.value.selectedPolyomino.cells[i].copy()
                _gameSate.update { state ->
                    state.copy(
                        selectedPolyomino = state.selectedPolyomino.copy(
                            cells = state.selectedPolyomino.cells.map { cell ->
                                Pair(
                                    cell.first - nextRotationPoint.first,
                                    cell.second - nextRotationPoint.second
                                )
                            }
                        )
                    )
                }
            }
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

    private fun findValidPosition(col: Int, row: Int, count: Int): MutableList<Pair<Int, Int>> {
        val positionen: MutableList<Pair<Int, Int>> = mutableListOf()
        if (count == 1 ) {
            _gameSate.value.selectedPolyomino.cells.forEach { cell ->
                if (cell.first + col in 0 until _gameSate.value.gridSize &&
                    cell.second + row in 0 until _gameSate.value.gridSize
                ) {
                    positionen.add(Pair(cell.first + col, cell.second + row))
                }
            }
        } else if (count.mod(5) != 0) {
            rotateRight()
            _gameSate.value.selectedPolyomino.cells.forEach { cell ->
                if (cell.first + col in 0 until _gameSate.value.gridSize &&
                    cell.second + row in 0 until _gameSate.value.gridSize
                ) {
                    positionen.add(Pair(cell.first + col, cell.second + row))
                }
            }
        } else {
            rotatePolyomino()
            _gameSate.value.selectedPolyomino.cells.forEach { cell ->
                if (cell.first + col in 0 until _gameSate.value.gridSize &&
                    cell.second + row in 0 until _gameSate.value.gridSize
                ) {
                    positionen.add(Pair(cell.first + col, cell.second + row))
                }
            }
        }


        if (checkValidPosition(positionen)) {
            _gameSate.update { state ->
                state.copy(
                    activPlayer = state.activPlayer.copy(
                        edges = state.activPlayer.edges + positonEdges(positionen)
                    )
                )
            }
            return positionen
        } else if (count <= 8) {
            return findValidPosition(col, row, count + 1)
        } else {
            return mutableListOf()
        }
    }


    private fun checkValidPosition(positionen: MutableList<Pair<Int, Int>>): Boolean {
        val updatedBoardGrid = _gameSate.value.boardGrid.copyOf()
        val points = _gameSate.value.selectedPolyomino.points
        if (positionen.size == 0) {
            return false
        }
        if (positionen.size != points) {
            return false
        }
        if (!_gameSate.value.activPlayer.edges.isEmpty() &&
            _gameSate.value.activPlayer_id == _gameSate.value.activPlayer.id &&
            !_gameSate.value.activPlayer.edges.contains(positionen[0].second * _gameSate.value.gridSize + positionen[0].first)
        ) {
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

    private fun rotatePolyomino() {
        if(_gameSate.value.selectedPolyomino.cells.isEmpty()) return
        val maxX = _gameSate.value.selectedPolyomino.cells.maxOf { it.first }
        _gameSate.update {
            it.copy(
                activPlayer = it.activPlayer.copy(
                    polyominos = it.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            polyomino.copy(
                                cells = adjustCoordinates(
                                    polyomino.cells.map { cell ->
                                        Pair(-cell.first + maxX, cell.second)
                                    }
                                )
                            )
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = it.selectedPolyomino.copy(
                    cells = it.selectedPolyomino.cells.map { cell ->
                        Pair(-cell.first, cell.second)
                    } ?: listOf()
                )
            )
        }
    }
    private fun rotateLeft() {
        if(_gameSate.value.selectedPolyomino.cells.isEmpty()) return
        val maxX = _gameSate.value.selectedPolyomino.cells.maxOf { it.first }

        _gameSate.update {
            it.copy(
                activPlayer = it.activPlayer.copy(
                    polyominos = it.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            polyomino.copy(
                                cells = adjustCoordinates(
                                    polyomino.cells.map { cell ->
                                        Pair(cell.second, -cell.first + maxX)
                                    }
                                )
                            )
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = it.selectedPolyomino.copy(
                    cells = it.selectedPolyomino.cells.map { cell ->
                            Pair(cell.second, -cell.first)
                    } ?: listOf()
                )
            )
        }
    }
    private fun rotateRight() {
        if(_gameSate.value.selectedPolyomino.cells.isEmpty()) return
        val maxX = _gameSate.value.selectedPolyomino.cells.maxOf { it.first }

        _gameSate.update {
            it.copy(
                activPlayer = it.activPlayer.copy(
                    polyominos = it.activPlayer.polyominos.map { polyomino ->
                        if (polyomino.isSelected) {
                            polyomino.copy(
                                cells = adjustCoordinates(
                                    polyomino.cells.map { cell ->
                                        Pair(cell.second, -cell.first + maxX)
                                    }
                                )
                            )
                        } else {
                            polyomino
                        }
                    }
                ),
                selectedPolyomino = it.selectedPolyomino.copy(
                    cells = it.selectedPolyomino.cells.map { cell ->
                        Pair(cell.second, -cell.first)
                    } ?: listOf()
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
                        polyomino.isSelected
                    }
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


    private fun positonEdges(positionen: MutableList<Pair<Int, Int>>): List<Int> {
        val edges: MutableList<Int> = mutableListOf()
        for (pos in positionen) {
            val index = pos.second * _gameSate.value.gridSize + pos.first
            if (index >= 14 && index.mod(14) != 0 && !edges.contains(index + 15)) edges.add(index + 15)
            if (index >= 14 && (index - 13).mod(14) != 0 && !edges.contains(index + 13)) edges.add(
                index + 13
            )
            if (index <= 195 && index.mod(14) != 0 && !edges.contains(index - 15)) edges.add(index - 15)
            if (index <= 195 && (index - 13).mod(14) != 0 && !edges.contains(index - 13)) edges.add(
                index - 13
            )
        }
//
        return edges
    }
    private fun adjustCoordinates(cells: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        // Finde die minimalen X- und Y-Werte
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }

        // Verschiebe alle Zellen, wenn minX oder minY negativ sind
        return cells.map { cell ->
            Pair(cell.first - minX.coerceAtMost(0), cell.second - minY.coerceAtMost(0))
        }
    }

}
