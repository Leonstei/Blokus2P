package com.example.blokus2p.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blokus2p.ai.AiInterface
import com.example.blokus2p.ai.MinmaxAi
import com.example.blokus2p.ai.RandomAi
import com.example.blokus2p.game.BlokusRules
import com.example.blokus2p.game.GameEngine
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.events.AppEvent
import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.events.PolyominoEvent
import com.example.blokus2p.game.BlokusBoard2
import com.example.blokus2p.helper.getUpdatedPlayerBitBoard
import com.example.blokus2p.model.PlayerType
import com.example.blokus2p.model.PlayerType.Human
import com.example.blokus2p.model.PlayerType.MinimaxAI
import com.example.blokus2p.model.PlayerType.MonteCarloAI
import com.example.blokus2p.model.PlayerType.RandomAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _gameSate = MutableStateFlow(GameState())
    val timerState: StateFlow<GameState> = _gameSate.asStateFlow()

    private val _polyominoState = MutableStateFlow(PolyominoSate())
    val polyominoState: StateFlow<PolyominoSate> = _polyominoState.asStateFlow()

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

    private fun handlePolyominoEvent(event: PolyominoEvent) {
        when (event) {
            is PolyominoEvent.PolyominoSelected -> {
                selectPolyomino(event.polyomino,event.selectedCell2)
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

    private fun handleGameEvent(event: GameEvent) {
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
                players = listOf(
                    Player(1, "Player 1",true, false, Color.Blue, 0,
                        availableEdges =  setOf(130)),
                    Player(2, "Player 2",false, true,Color.Magenta, 0,
                        availableEdges =  setOf(65),isAi = true, ai = MinmaxAi())),
                activPlayer_id = 1,
                activPlayer = Player(1, "Player 1",true, false,Color.Blue, 0, availableEdges =  setOf(143)),
                playerOneColor = Color.Blue,
                playerTwoColor = Color.Magenta,
                board = BlokusBoard2()
            )
        }
        val activPlayer = _gameSate.value.players.first { player -> player.id == _gameSate.value.activPlayer_id }
        _gameSate.update {
            it.copy(
                players = it.players.map { player ->
                    player.copy(availableMoves = GameEngine().calculateAllMovesOfAPlayer(
                            player,
                            BlokusBoard2(),
                            rules))
                },
                activPlayer = activPlayer.copy(availableMoves = GameEngine().calculateAllMovesOfAPlayer(
                    activPlayer,
                    BlokusBoard2(),
                    rules))
            )
        }
    }

    private fun placePolyomino(col: Int, row: Int) {
        if (_gameSate.value.selectedPolyomino.name == "") return
        setSelectedCellFirst()
        val newBoard = GameEngine().place(
            _gameSate.value.activPlayer,
            _gameSate.value.selectedPolyomino,
            col + row *_gameSate.value.board.boardSize,
            _gameSate.value.board, rules
        )
        if (newBoard != null){
            updateBoard(newBoard)
            updatePolyominosOfActivPlayer(_gameSate.value.activPlayer_id)
            updateAvailableEdgesActivPlayer()
            updateAvailableMoves()
            nextPlayer(_gameSate.value.activPlayer_id)
            checkForAiTurn()
        }
    }


    private fun checkForAiTurn() {
        val activePlayer = _gameSate.value.activPlayer
        if (activePlayer.isAi && activePlayer.ai != null) {
            viewModelScope.launch {
                val aiMove = _gameSate.value.activPlayer.ai?.getNextMove(_gameSate.value)
                Log.d("AppViewModel", "aiMove $aiMove")
                aiMove?.let {
                    selectPolyomino(
                        aiMove.polyomino,
                        aiMove.position
                    )
                    val newBoard = GameEngine().placeAiMove(
                        _gameSate.value.activPlayer,
                        aiMove.polyomino,
                        aiMove.position,
                        _gameSate.value.board, rules, aiMove.orientation
                    )
                    updateBoard(newBoard)
                    updatePolyominosOfActivPlayer(_gameSate.value.activPlayer_id)
                    updateAvailableEdgesActivPlayer()
                    updateAvailableMoves()
                    nextPlayer(_gameSate.value.activPlayer_id)
                }
            }
        }
    }

    private fun updatePolyominosOfActivPlayer(currentPlayerId: Int){
        _gameSate.update { gameSate->
            val updatedPlayers = gameSate.players.map { player ->
                if (player.id == currentPlayerId) {
                    player.copy(
                        newPolyominos = player.newPolyominos.filterNot { it.name == player.placedPolyomino.name }
                    )
                } else {
                    player
                }
            }
            gameSate.copy(
                players = updatedPlayers,
                activPlayer = updatedPlayers.first { it.id == currentPlayerId }
            )
        }
    }


    private fun nextPlayer(currentPlayerId: Int) {
        _gameSate.update { gameState ->
            // Liste der Spieler holen und kopieren
            val updatedPlayers = gameState.players.map { player ->
                if (player.id == currentPlayerId) {
                    player.copy(
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
                    isAi = playerOneType != Human,
                    ai = getAiByTyp(playerOneType)))
            if(!player.isActiv) updatedPlayers.add(
                player.copy(name = namePlayerTwo, color = colorPlayerTwo,
                    isAi = playerTwoType != Human,
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
        if(selected.name == "")return
        val updatedPolyomino = selected.flippHorizontal()
        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    newPolyominos = state.activPlayer.newPolyominos.map { polyomino ->
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
        if(selected.name == "")return
        val updatedPolyomino = selected.rotatedLeft()

        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    newPolyominos = state.activPlayer.newPolyominos.map { polyomino ->
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
        if(selected.name == "")return
        val updatedPolyomino = selected.rotatedRight()

        _gameSate.update { state ->
            state.copy(
                activPlayer = state.activPlayer.copy(
                    newPolyominos = state.activPlayer.newPolyominos.map { polyomino ->
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

    private fun selectPolyomino(polyomino: Polyomino,selectedCell : Int){
        _gameSate.update { state ->
            state.copy(
                selectedPolyomino = polyomino.copy(
                    selectedCell2 = selectedCell
                ),
                activPlayer = _gameSate.value.activPlayer.copy(
                    newPolyominos = _gameSate.value.activPlayer.newPolyominos.map {
                        if (it.name == polyomino.name) {
                            it.copy(isSelected = true,
                                selectedCell2 = selectedCell
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
                    cells2 = state.selectedPolyomino.currentVariant.map { cell->
                        cell - state.selectedPolyomino.selectedCell2
                    }
//                    cells = state.selectedPolyomino.currentVariant.map { cell->
//                        Pair(cell.first - _gameSate.value.selectedPolyomino.selectedCell.first
//                            ,cell.second - _gameSate.value.selectedPolyomino.selectedCell.second)
//                    }
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
    private fun updateAvailableMoves() {
        val activePlayer = _gameSate.value.activPlayer
        val opponentPlayer = _gameSate.value.players.first { player -> player.id != activePlayer.id }
        val board = _gameSate.value.board
        val availableMoves = activePlayer.availableMoves

        val allAvailableMoves = GameEngine().calculateAllMovesOfAPlayer(activePlayer, board, rules)
        val notAvailableMoves = GameEngine().calculateNotAvailableMoves(activePlayer, board )
        val opponentNotAvailableMoves = GameEngine().calculateNotAvailableMoves(opponentPlayer, board)

        val newAvailableMoves = GameEngine().calculateNewMoves(activePlayer, board, rules)
//        newAvailableMoves.forEach { move->
//            if(!allAvailableMoves.contains(move)){
//                Log.d("AppViewModel", "move not in allMoves but in newMoves $move")
//            }
//        }

        //val notAvailableMoves = GameEngine().calculateNotAvailableMovesOptimized(activePlayer, _gameSate.value.board)
        val finalMoves = (availableMoves.plus(newAvailableMoves).minus(notAvailableMoves.toSet()))//.sortedByDescending { move ->
//            move.polyomino.points
//        }.toSet()
//        if (allAvailableMoves.size < finalMoves.size ){
//            Log.d("AppViewModel", "all moves: $allAvailableMoves")
//            Log.d("AppViewModel", "finalMoves : $finalMoves")
//            Log.d("AppViewModel", "new Moves : $newAvailableMoves")
//            Log.d("AppViewModel", "not moves2 : $notAvailableMoves2")
//        }
        val updatedPlayer = activePlayer.copy(
            availableMoves = allAvailableMoves
        )
        val newPlayers = _gameSate.value.players.map { player ->
            if (player.id == updatedPlayer.id) updatedPlayer else if (player.id == opponentPlayer.id) {
                player.copy(
//                    availableMoves = (player.availableMoves.minus(opponentNotAvailableMoves.toSet())).sortedByDescending { move ->
//                        move.polyomino.points
//                    }.toSet()
                )
            } else {
                player
            }
        }
        _gameSate.update { state ->
            state.copy(
                players = newPlayers,
                activPlayer = updatedPlayer
            )
        }
        Log.d("AppViewModel", "available moves after update ${_gameSate.value.activPlayer.availableMoves.size}")

    }

    private fun updateBoard(newBoard: BlokusBoard2?) {
        if (newBoard != null) {
            val gameState = _gameSate.value
            val updatedPlayerBitBoard = getUpdatedPlayerBitBoard(gameState.board.boardGrid, newBoard.boardGrid,gameState.activPlayer.bitBoard,)
            val updatedPlayer = gameState.activPlayer.copy(
                points =  gameState.activPlayer.points + gameState.selectedPolyomino.points,
                placedPolyomino = gameState.selectedPolyomino.copy(),
                bitBoard = updatedPlayerBitBoard
            )
            val newPlayers = gameState.players.map { player ->
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

    private fun updateBoardUndo(newBoard: BlokusBoard2?){
        // Hier wird der letzte Zug rückgängig gemacht
        // es könnte sein das availableEdges und availableMoves nicht mehr stimmen
        if (newBoard != null) {
            val lastPlacedPolyomino = _gameSate.value.board.placedPolyominos.lastOrNull()
            if (lastPlacedPolyomino != null){
                if (lastPlacedPolyomino.playerId != _gameSate.value.activPlayer.id) nextPlayer(_gameSate.value.activPlayer_id)
                var newPolyominos = _gameSate.value.activPlayer.newPolyominos
                newPolyominos  = mutableListOf(lastPlacedPolyomino.polyomino).plus(newPolyominos)
                val newAvailableEdges = GameEngine().calculateNewAvailableEdges(_gameSate.value.activPlayer, newBoard)

                val updatedPlayer = _gameSate.value.activPlayer.copy(
                    newPolyominos = newPolyominos ,
                    points = _gameSate.value.activPlayer.points - lastPlacedPolyomino.polyomino.points,
                    placedPolyomino =  Polyomino(),
                    availableEdges = newAvailableEdges
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
        } else {
            Log.d("AppViewModel", "Undo konnte nicht ausgeführt werden")
        }
    }

    private fun getAiByTyp(playerType: PlayerType): AiInterface?{
        return when (playerType){
            Human -> null
            MinimaxAI -> MinmaxAi()
            RandomAI -> RandomAi()
            MonteCarloAI -> RandomAi()
        }
    }
}
