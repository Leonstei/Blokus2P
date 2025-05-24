package com.example.blokus2p.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
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

class AppViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

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
        _gameState.update {
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
                board = BlokusBoard()
            )
        }
        val activPlayer = _gameState.value.players.first { player -> player.id == _gameState.value.activPlayer_id }
        _gameState.update {
            it.copy(
                players = it.players.map { player ->
                    player.copy(availableMoves = GameEngine().calculateAllMovesOfAPlayer(
                            player,
                            _gameState.value.board,
                            rules))
                },
                activPlayer = activPlayer.copy(availableMoves = GameEngine().calculateAllMovesOfAPlayer(
                    activPlayer,
                    _gameState.value.board,
                    rules))
            )
        }
    }

    private fun placePolyomino(col: Int, row: Int) {
        if (_gameState.value.selectedPolyomino.name == "") return
        setSelectedCellFirst()
        val newBoard = GameEngine().place(
            _gameState.value.activPlayer,
            _gameState.value.selectedPolyomino,
            col,
            row,_gameState.value.board, rules
        )
        if (newBoard != null){
            updateBoard(newBoard)
            updatePolyominosOfActivPlayer(_gameState.value.activPlayer_id)
            updateAvailableEdgesActivPlayer()
            updateAvailableMoves()
            nextPlayer(_gameState.value.activPlayer_id)
            checkForAiTurn()
        }
    }


    fun checkForAiTurn() {
        val activePlayer = _gameState.value.activPlayer
        if (activePlayer.isAi && activePlayer.ai != null) {
//            viewModelScope.launch {
//                val timeTaken = measureTime {
//                    val aiMove = _gameSate.value.activPlayer.ai?.getNextMove(_gameSate.value)
//                }
//                Log.d("AppViewModel", "AI took $timeTaken")
                val aiMove = _gameState.value.activPlayer.ai?.getNextMove(_gameState.value)
                //Log.d("AppViewModel", "aiMove $aiMove")
                if(aiMove == null && activePlayer.availableMoves.isNotEmpty()) {
                    val notValidMoves =  GameEngine().calculateNotAvailableMoves2(activePlayer,activePlayer.availableMoves, _gameState.value.board, rules)
                    if(notValidMoves.size == activePlayer.availableMoves.size){
                        _gameState.update { state ->
                            state.copy(
                                activPlayer = activePlayer.copy(
                                    availableMoves = activePlayer.availableMoves.minus(notValidMoves.toSet())
                                )
                            )
                        }
                        nextPlayer(activePlayer.id)
                    }
                    println("aiMove $aiMove")
                    print("available moves ${_gameState.value.activPlayer.availableMoves}")
                    println("player ${_gameState.value.activPlayer.name} has no available moves")
                    println("notValidMoves ${notValidMoves}")
                }
                aiMove?.let {
                    selectPolyomino(
                        aiMove.polyomino,
                        Pair(aiMove.position.first, aiMove.position.second)
                    )
                    val newBoard = GameEngine().placeAiMove(
                        _gameState.value.activPlayer,
                        aiMove.polyomino,
                        aiMove.position.first,
                        aiMove.position.second, _gameState.value.board, rules, aiMove.orientation
                    )
                    updateBoard(newBoard)
                    updatePolyominosOfActivPlayer(_gameState.value.activPlayer_id)
                    updateAvailableEdgesActivPlayer()
                    updateAvailableMoves()
                    nextPlayer(_gameState.value.activPlayer_id)
                }

        }
    }

    private fun updatePolyominosOfActivPlayer(currentPlayerId: Int){
        _gameState.update { gameSate->
            val updatedPlayers = gameSate.players.map { player ->
                if (player.id == currentPlayerId) {
                    player.copy(
                        polyominos = player.polyominos.filterNot { it.name == player.placedPolyomino.name }
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
        _gameState.update { gameState ->
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
            val nextIndex = getNextPlayerIndex(currentIndex, updatedPlayers)
            val nextPlayer = updatedPlayers[nextIndex]
            val isGameOver = updatedPlayers.all { player ->
                player.polyominos.isEmpty() || player.availableMoves.isEmpty() || player.availableEdges.isEmpty()
            }

            // Liste aktualisieren mit dem neuen aktiven Spieler
            val finalPlayers = updatedPlayers.map {
                if (it.id == nextPlayer.id) it.copy(isActiv = true)
                else it
            }

            gameState.copy(
                isFinished = isGameOver,
                players = finalPlayers,
                activPlayer = nextPlayer.copy(isActiv = true),
                activPlayer_id = nextPlayer.id,
                selectedPolyomino = Polyomino()
            )
        }
    }
    private fun getNextPlayerIndex(currentPlayerIndex: Int, updatedPlayers: List<Player>): Int {
        val nextIndex = (currentPlayerIndex + 1) % updatedPlayers.size
        val nextPlayer = updatedPlayers[nextIndex]
        val isNextPlayerDone = nextPlayer.availableMoves.isEmpty() || nextPlayer.polyominos.isEmpty() || nextPlayer.availableEdges.isEmpty()
        if(isNextPlayerDone) {
            return currentPlayerIndex
        }else {
            return nextIndex
        }
    }



    private fun undoPlace(){
        val newBoard = GameEngine().undoplace(_gameState.value.board)
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
        _gameState.value.players.map { player ->
            if (player.isActiv) updatedPlayers.add(
                player.copy( name = namePlayerOne, color = colorPlayerOne,
                    isAi = playerOneType != Human,
                    ai = getAiByTyp(playerOneType)))
            if(!player.isActiv) updatedPlayers.add(
                player.copy(name = namePlayerTwo, color = colorPlayerTwo,
                    isAi = playerTwoType != Human,
                    ai = getAiByTyp(playerTwoType)))
        }
        _gameState.update { state ->
            state.copy(
                activPlayer = _gameState.value.activPlayer.copy(
                    name = namePlayerOne,
                    color = colorPlayerOne,
                    isAi = playerOneType != Human,
                    ai = getAiByTyp(playerOneType)
                ),
                players = updatedPlayers,
                playerOneColor = colorPlayerOne,
                playerTwoColor = colorPlayerTwo
            )
        }
    }

    private fun flippPolyomino() {
        val selected = _gameState.value.selectedPolyomino
        if(selected.name == "")return
        val updatedPolyomino = selected.flippHorizontal()
        _gameState.update { state ->
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
        val selected = _gameState.value.selectedPolyomino
        if(selected.name == "")return
        val updatedPolyomino = selected.rotatedLeft()

        _gameState.update { state ->
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
        val selected = _gameState.value.selectedPolyomino
        if(selected.name == "")return
        val updatedPolyomino = selected.rotatedRight()

        _gameState.update { state ->
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
        _gameState.update { state ->
            state.copy(
                selectedPolyomino = polyomino.copy(
                    selectedCell = selectedCell
                ),
                activPlayer = _gameState.value.activPlayer.copy(
                    polyominos = _gameState.value.activPlayer.polyominos.map {
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
        _gameState.update { state->
            state.copy(
                selectedPolyomino = state.selectedPolyomino.copy(
                    cells = state.selectedPolyomino.currentVariant.map { cell->
                        Pair(cell.first - _gameState.value.selectedPolyomino.selectedCell.first
                            ,cell.second - _gameState.value.selectedPolyomino.selectedCell.second)
                    }
                )
            )
        }
    }


    private fun updateAvailableEdgesActivPlayer(){
        val avilableEdges = _gameState.value.activPlayer.availableEdges
        val newAvilableEdges = GameEngine().calculateNewAvailableEdges(_gameState.value.activPlayer, _gameState.value.board)
        val notAvilableEdges = GameEngine().notCheckForNotAvailableEdges(avilableEdges, _gameState.value.board)
        val updatedPlayer = _gameState.value.activPlayer.copy(
            availableEdges = avilableEdges.plus(newAvilableEdges).minus(notAvilableEdges)
        )
        val newPlayers = _gameState.value.players.map { player ->
            if (player.id == updatedPlayer.id) updatedPlayer else player
        }
        _gameState.update { state ->
            state.copy(
                players = newPlayers,
                activPlayer = updatedPlayer
            )
        }
        //Log.d("AppViewModel", "available edges ${_gameState.value.activPlayer.availableEdges}")
    }
    private fun updateAvailableMoves() {
        val activePlayer = _gameState.value.activPlayer
        val opponentPlayer = _gameState.value.players.first { player -> player.id != activePlayer.id }
        val board = _gameState.value.board
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
        val finalMoves = (availableMoves.plus(newAvailableMoves).minus(notAvailableMoves.toSet())).sortedByDescending { move ->
            move.polyomino.points
        }.toSet()
//        if (allAvailableMoves.size < finalMoves.size ){
//            Log.d("AppViewModel", "all moves: $allAvailableMoves")
//            Log.d("AppViewModel", "finalMoves : $finalMoves")
//            Log.d("AppViewModel", "new Moves : $newAvailableMoves")
//            Log.d("AppViewModel", "not moves2 : $notAvailableMoves2")
//        }
        val updatedPlayer = activePlayer.copy(
            availableMoves = finalMoves
        )
        val newPlayers = _gameState.value.players.map { player ->
            if (player.id == updatedPlayer.id) updatedPlayer else if (player.id == opponentPlayer.id) {
                player.copy(
                    availableMoves = (player.availableMoves.minus(opponentNotAvailableMoves.toSet())).sortedByDescending { move ->
                        move.polyomino.points
                    }.toSet())
            } else {
                player
            }
        }
        _gameState.update { state ->
            state.copy(
                players = newPlayers,
                activPlayer = updatedPlayer
            )
        }
        //Log.d("AppViewModel", "available moves after update ${_gameState.value.activPlayer.availableMoves.size}")

    }

    private fun updateBoard(newBoard: GameBoard?) {
        if (newBoard != null) {
            val updatedPlayer = _gameState.value.activPlayer.copy(
                points =  _gameState.value.activPlayer.points + _gameState.value.selectedPolyomino.points,
                placedPolyomino = _gameState.value.selectedPolyomino.copy()
            )

            val newPlayers = _gameState.value.players.map { player ->
                if (player.id == updatedPlayer.id) updatedPlayer else player
            }
            _gameState.update { state ->
                state.copy(
                    players = newPlayers,
                    board = newBoard,
                    activPlayer = updatedPlayer
                )
            }
        } else {
            //Log.d("AppViewModel", "Polyomino konnte nicht platziert werden")
        }
    }
    private fun updateBoardUndo(newBoard: GameBoard?){
        // Hier wird der letzte Zug rückgängig gemacht
        // es könnte sein das availableEdges und availableMoves nicht mehr stimmen
        if (newBoard != null) {
            val lastPlacedPolyomino = _gameState.value.board.placedPolyominos.lastOrNull()
            if (lastPlacedPolyomino != null){
                if (lastPlacedPolyomino.playerId != _gameState.value.activPlayer.id) nextPlayer(_gameState.value.activPlayer_id)
                var newPolyominos = _gameState.value.activPlayer.polyominos
                newPolyominos  = mutableListOf(lastPlacedPolyomino.polyomino).plus(newPolyominos)
                val newAvailableEdges = GameEngine().calculateNewAvailableEdges(_gameState.value.activPlayer, newBoard)

                val updatedPlayer = _gameState.value.activPlayer.copy(
                    polyominos = newPolyominos ,
                    points = _gameState.value.activPlayer.points - lastPlacedPolyomino.polyomino.points,
                    placedPolyomino =  Polyomino(),
                    availableEdges = newAvailableEdges
                )
                val newPlayers = _gameState.value.players.map { player ->
                    if (player.id == updatedPlayer.id) updatedPlayer else player
                }
                _gameState.update { state ->
                    state.copy(
                        players = newPlayers,
                        board = newBoard,
                        activPlayer = updatedPlayer
                    )
                }
            }
        } else {
//            Log.d("AppViewModel", "Undo konnte nicht ausgeführt werden")
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
