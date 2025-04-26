package com.example.blokus2p.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.blokus2p.ai.AiInterface
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
import com.example.blokus2p.model.PlayerType
import com.example.blokus2p.model.PlayerType.Human
import com.example.blokus2p.model.PlayerType.MinimaxAI
import com.example.blokus2p.model.PlayerType.MonteCarloAI
import com.example.blokus2p.model.PlayerType.RandomAI
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
        setInitialGameState()
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
                setInitialGameState()
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
                changeSettings(event.nameActivPlayer, event.colorActivPlayer, event.namePlayerTwo, event.colorPlayerTwo, event.playerOneType, event.playerTwoType)
            }
        }
    }
    private fun setInitialGameState() {
        _gameSate.update {
            it.copy(
                players = listOf(Player(1, "Player 1",true, Color.Blue, 0, availableEdges =  setOf(143)),
                    Player(2, "Player 2",false, Color.Magenta, 0,availableEdges =  setOf(52),isAi = true, ai = RandomAi())),
                activPlayer_id = 1,
                activPlayer = Player(1, "Player 1",true, Color.Blue, 0, availableEdges =  setOf(143)),
                playerOneColor = Color.Blue,
                playerTwoColor = Color.Magenta,
                board = BlokusBoard()
            )
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
        checkForAiTurn()
    }

    private fun checkForAiTurn() {
        val activePlayer = _gameSate.value.activPlayer
        if (activePlayer.isAi && activePlayer.ai != null) {
            updateAvailableEdgesActivPlayer()
            val aiMove = _gameSate.value.activPlayer.ai!!.getNextMove(_gameSate.value)
            Log.d("AppViewModel", "aiMove ${aiMove}")
            if(aiMove == null) Log.d("AppViewModel", "available edges ${activePlayer.availableEdges}")
            aiMove?.let {
                selectPolyomino(aiMove.polyomino, Pair(aiMove.position.first, aiMove.position.second))
                val newBoard = GameEngine().placeAiMove(
                    _gameSate.value.activPlayer,
                    aiMove.polyomino,
                    aiMove.position.first,
                    aiMove.position.second, _gameSate.value.board, rules, aiMove.orientation
                )
                updateBoard(newBoard)
                nextPlayer(_gameSate.value.activPlayer_id)
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

    private fun changeSettings(
        namePlayerOne: String,
        colorPlayerOne: Color,
        namePlayerTwo: String,
        colorPlayerTwo: Color,
        playerOneType: PlayerType,
        playerTwoType: PlayerType
    ){
        val updatedPlayers :MutableList<Player> = mutableListOf()
        _gameSate.value.players.map { player ->
            if (player.isActiv) updatedPlayers.add(
                player.copy( name = namePlayerOne, color = colorPlayerOne,
                    isAi = if (playerOneType != Human) true else false,
                    ai = getAiByTyp(playerOneType)))
            if(!player.isActiv) updatedPlayers.add(
                player.copy(name = namePlayerTwo, color = colorPlayerTwo,
                    isAi = if (playerTwoType != Human) true else false,
                    ai = getAiByTyp(playerTwoType)))
        }
        _gameSate.update { state ->
            state.copy(
                activPlayer = _gameSate.value.activPlayer.copy(
                    name = namePlayerOne,
                    color = colorPlayerOne
                ),
                players = updatedPlayers,
                playerOneColor = colorPlayerOne,
                playerTwoColor = colorPlayerTwo
            )
        }
    }

    private fun flippPolyomino() {
        val selected = _gameSate.value.selectedPolyomino
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
                    cells = state.selectedPolyomino.currentVariant.map { cell->
                        Pair(cell.first - _gameSate.value.selectedPolyomino.selectedCell.first
                            ,cell.second - _gameSate.value.selectedPolyomino.selectedCell.second)
                    }
                )
            )
        }
    }


    private fun updateAvailableEdgesActivPlayer(){
        val avilableEdges = _gameSate.value.activPlayer.availableEdges
        val newAvilableEdges = GameEngine().calculateNewAvailableEdges(_gameSate.value.activPlayer, _gameSate.value.board)
        val notAvilableEdges = GameEngine().notCheckForNotAvailableEdges(avilableEdges, _gameSate.value.board)
        val updatedPlayer = _gameSate.value.activPlayer.copy(
            availableEdges = avilableEdges.plus(newAvilableEdges).minus(notAvilableEdges)
        )
        val newPlayers = _gameSate.value.players.map { player ->
            if (player.id == updatedPlayer.id) updatedPlayer else player
        }
        _gameSate.update { state ->
            state.copy(
                players = newPlayers,
                activPlayer = updatedPlayer
            )
        }
        Log.d("AppViewModel", "available edges ${_gameSate.value.activPlayer.availableEdges}")
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

    fun getAiByTyp(playerType: PlayerType,): AiInterface?{
        when (playerType){
            Human -> return null
            MinimaxAI -> return RandomAi()
            RandomAI -> return RandomAi()
            MonteCarloAI -> return RandomAi()
        }
    }
}
