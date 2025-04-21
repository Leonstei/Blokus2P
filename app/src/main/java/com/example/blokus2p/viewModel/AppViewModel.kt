package com.example.blokus2p.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blokus2p.ai.RandomAi
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.events.AppEvent
import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.events.PolyominoEvent
import com.example.blokus2p.game.BlokusBoard
import com.example.blokus2p.game.GameBoard
import com.example.blokus2p.model.Move
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.LinkedList

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
                players = listOf(Player(1, "Player 1",true, Color.Blue, 0, availableEdges =  listOf(143)),
                    Player(2, "Player 2",false, Color.Magenta, 0,availableEdges =  listOf(52),isAi = true, ai = RandomAi())),
                activPlayer_id = 1,
                activPlayer = Player(1, "Player 1",true, Color.Blue, 0),
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
                selectPolyomino(event.polyomino,event.selectedCell)
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
                        players = listOf(Player(1, event.nameActivPlayer, true, event.colorActivPlayer, 0),Player(2, event.namePlayerTwo, false,  event.colorPlayerTwo, 0)),
                        activPlayer_id = 1,
                        isFinished = false,
                        activPlayer = Player(1, event.nameActivPlayer, false,  event.colorActivPlayer, 0),
                        playerOneColor = event.colorActivPlayer,
                        playerTwoColor = event.colorPlayerTwo,
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
                changeSettings(event.nameActivPlayer, event.colorActivPlayer, event.namePlayerTwo, event.colorPlayerTwo)
            }
        }
    }

    private fun placePolyomino(col: Int, row: Int) {
        if (_gameSate.value.selectedPolyomino.name == "") return
        setSelectedCellFirst()
        val newBoard = GameEngine().place(
            _gameSate.value.activPlayer,
            _gameSate.value.selectedPolyomino,
            col,
            row,_gameSate.value.board, rules
        )
        updateBoard(newBoard)

        updateAvailableEdgesActivPlayer()
        nextPlayer(_gameSate.value.activPlayer_id)
        updateAvailableEdgesActivPlayer()
        checkForAiTurn()
    }

    private fun checkForAiTurn() {
        val activePlayer = _gameSate.value.activPlayer
        if (activePlayer.isAi && activePlayer.ai != null) {
            val aiMove = _gameSate.value.activPlayer.ai!!.getNextMove(_gameSate.value)
            Log.d("AppViewModel", "aiMove ${aiMove}")
            aiMove?.let {
//                val newBoard = GameEngine().place(
//                    _gameSate.value.activPlayer,
//                    aiMove.polyomino,
//                    aiMove.position.first,
//                    aiMove.position.second,_gameSate.value.board, rules
//                )
//                updateBoard(newBoard)
//                nextPlayer(_gameSate.value.activPlayer_id)
            }
        }
    }


    private fun nextPlayer(currentPlayerId: Int) {
        _gameSate.update { gameState ->
            // Liste der Spieler holen und kopieren
            val updatedPlayers = gameState.players.map { player ->
                if (player.id == currentPlayerId) {
                    player.copy(
                        polyominos = player.polyominos.filterNot { it.name == player.placedPolyomino.name },
                        polyominoIsPlaced = false,
                        isActiv = false
                    )
                } else {
                    player
                }
            }

            // Neuen aktiven Spieler bestimmen
            val currentIndex = updatedPlayers.indexOfFirst { it.id == currentPlayerId }
            val nextIndex = (currentIndex + 1) % updatedPlayers.size
            val nextPlayer = updatedPlayers[nextIndex]

            // Liste aktualisieren mit dem neuen aktiven Spieler
            val finalPlayers = updatedPlayers.map {
                if (it.id == nextPlayer.id) it.copy(isActiv = true)
                else it
            }

            gameState.copy(
                players = finalPlayers,
                activPlayer = nextPlayer.copy(isActiv = true),
                activPlayer_id = nextPlayer.id,
                selectedPolyomino = Polyomino()
            )
        }
    }



    private fun undoPlace(){
        val newBoard = GameEngine().undoplace(_gameSate.value.board)
        updateBoardUndo(newBoard)
    }

    private fun changeSettings(namePlayerOne:String, colorPlayerOne:Color, namePlayerTwo: String, colorPlayerTwo: Color ){
        val updatedPlayers :MutableList<Player> = mutableListOf()
        _gameSate.value.players.map { player ->
            if(player.isActiv) updatedPlayers.add( player.copy(name = namePlayerOne, color = colorPlayerOne))
            if(!player.isActiv) updatedPlayers.add( player.copy(name = namePlayerTwo, color = colorPlayerTwo))
        }
        updateGameState(
            activPlayer = _gameSate.value.activPlayer.copy(
                name = namePlayerOne,
                color = colorPlayerOne
            ),
            players = updatedPlayers,
            playerOneColor = colorPlayerOne,
            playerTwoColor = colorPlayerTwo
        )
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

    private fun selectPolyomino(polyomino: Polyomino,selectedCell : Pair<Int,Int>){
        _gameSate.update { state ->
            state.copy(
                selectedPolyomino = polyomino.copy(
                    selectedCell = selectedCell
                ),
                activPlayer = _gameSate.value.activPlayer.copy(
                    polyominos = _gameSate.value.activPlayer.polyominos.map {
                        if (it.name == polyomino.name) {
                            it.copy(isSelected = true,
                                selectedCell = selectedCell
                            )
                        } else {
                            it.copy(isSelected = false)
                        }
                    }
                )
            )
        }
    }

    private fun setSelectedCellFirst(){
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

    private fun setSelectedCellFirstAi(){
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

    private fun updateAvailableEdgesActivPlayer(){
        val avilableEdges = GameEngine().calculateNewAvailableEdges(_gameSate.value.activPlayer,_gameSate.value.board)
        _gameSate.update { state ->
            state.copy(
                activPlayer = _gameSate.value.activPlayer.copy(availableEdges = avilableEdges)
            )
        }
    }

    private fun updateBoard(newBoard: GameBoard?) {
        if (newBoard != null) {
            val updatedPlayer = _gameSate.value.activPlayer.copy(
                points =  _gameSate.value.activPlayer.points + _gameSate.value.selectedPolyomino.points,
                placedPolyomino = _gameSate.value.selectedPolyomino.copy()
            )

            val newPlayers = _gameSate.value.players.map { player ->
                if (player.id == updatedPlayer.id) updatedPlayer else player
            }
            _gameSate.update { state ->
                state.copy(
                    players = newPlayers,
                    board = newBoard,
                    activPlayer = updatedPlayer
                )
            }
        } else {
            Log.d("AppViewModel", "Polyomino konnte nicht platziert werden")
        }
    }
    private fun updateBoardUndo(newBoard: GameBoard?){
        if (newBoard != null) {
            val lastPlacedPolyomino = _gameSate.value.board.placedPolyominos.lastOrNull()
            if (lastPlacedPolyomino != null){
                if (lastPlacedPolyomino.playerId != _gameSate.value.activPlayer.id) nextPlayer(_gameSate.value.activPlayer_id)
                var newPolyominos = _gameSate.value.activPlayer.polyominos
                newPolyominos  = mutableListOf(lastPlacedPolyomino.polyomino).plus(newPolyominos)

                val updatedPlayer = _gameSate.value.activPlayer.copy(
                    polyominos = newPolyominos ,
                    points = _gameSate.value.activPlayer.points - lastPlacedPolyomino.polyomino.points,
                    placedPolyomino =  Polyomino()
                )
                val newPlayers = _gameSate.value.players.map { player ->
                    if (player.id == updatedPlayer.id) updatedPlayer else player
                }
                _gameSate.update { state ->
                    state.copy(
                        players = newPlayers,
                        board = newBoard,
                        activPlayer = updatedPlayer
                    )
                }
            }

//            Log.d("AppViewModel", "Activ Player = ${_gameSate.value.activPlayer}")
//            Log.d("AppViewModel", "board = ${_gameSate.value.board}")
        } else {
            Log.d("AppViewModel", "Undo konnte nicht ausgeführt werden")
        }
    }

    private fun updateGameState(
        board: GameBoard? = null,
        activPlayer: Player? = null,
        activPlayerPoints: Int? = null,
        placedPolyomino: Polyomino? = null,
        selectedPolyomino: Polyomino? = null,
        activPlayerId: Int? = null,
        players: List<Player>? = null,
        playerOneColor: Color? = null,
        playerTwoColor: Color? = null
    ) {
        _gameSate.update { state ->
            val updatedActivPlayer = when {
                activPlayer != null -> activPlayer
                activPlayerPoints != null || placedPolyomino != null -> {
                    state.activPlayer.copy(
                        points = activPlayerPoints ?: state.activPlayer.points,
                        placedPolyomino = placedPolyomino ?: state.activPlayer.placedPolyomino
                    )
                }
                else -> state.activPlayer
            }

            state.copy(
                board = board ?: state.board,
                activPlayer = updatedActivPlayer,
                selectedPolyomino = selectedPolyomino ?: state.selectedPolyomino,
                activPlayer_id = activPlayerId ?: state.activPlayer_id,
                players = players ?: state.players,
                playerOneColor = playerOneColor ?: state.playerOneColor,
                playerTwoColor = playerTwoColor ?: state.playerTwoColor

            )
        }
    }
}
