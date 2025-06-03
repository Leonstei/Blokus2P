//#include <jni.h>
//#include <string>
//#include <vector>
//#include <chrono> // Für Timeout
//
//// --- Eigene C++-Klassen für Blokus-Spielzustand und MCTS ---
//// Diese Klassen musst du selbst implementieren.
//// Hier sind Platzhalter-Definitionen zur Veranschaulichung.
//
//// Angenommen, du hast eine Klasse für den Spielzustand
//// die effizient für Simulationen ist.
//class BlokusGameState {
//public:
//    // Spielfeld: z.B. 0=leer, 1=Spieler1, 2=Spieler2, etc.
//    std::vector<int> board; // 20x20 = 400 Elemente
//    std::vector<std::vector<bool>> piecesAvailable; // [numPlayers][21]
//    int currentPlayer;
//    int numPlayers; // Anzahl der Spieler im Spiel
//
//    BlokusGameState(const std::vector<int>& initialBoard,
//                    const std::vector<std::vector<bool>>& initialPiecesAvailable,
//                    int current, int numPl)
//            : board(initialBoard), piecesAvailable(initialPiecesAvailable),
//              currentPlayer(current), numPlayers(numPl) {}
//
//    // Wichtige Methoden für MCTS:
//    // 1. Alle möglichen Züge vom aktuellen Zustand generieren
//    std::vector<std::vector<int>> getPossibleMoves() const {
//        // Implementiere Logik, um alle gültigen Blokus-Züge zu finden
//        // Rückgabe: Liste von [pieceId, row, col, rotation, flip]
//        // Beispiel:
//        std::vector<std::vector<int>> moves;
//        // if (board[0] == 0) moves.push_back({0, 0, 0, 0, 0}); // Nur als Beispiel
//        return moves;
//    }
//
//    // 2. Einen Zug ausführen (erzeugt einen neuen Spielzustand)
//    BlokusGameState applyMove(const std::vector<int>& move) const {
//        BlokusGameState newState = *this; // Kopiere aktuellen Zustand
//        // Logik, um den Zug auf newState anzuwenden
//        // currentPlayer auf den nächsten Spieler setzen
//        newState.currentPlayer = (newState.currentPlayer + 1) % numPlayers;
//        return newState;
//    }
//
//    // 3. Überprüfen, ob das Spiel beendet ist
//    bool isGameOver() const {
//        // Logik, um zu prüfen, ob das Spiel beendet ist (keine Züge mehr möglich für alle Spieler)
//        return false;
//    }
//
//    // 4. Den Gewinner ermitteln (für Backpropagation)
//    int getWinner() const {
//        // Logik, um den Gewinner zu ermitteln (z.B. Spieler mit den meisten Punkten)
//        return -1; // Oder den Index des Gewinners
//    }
//};
//
//// Knoten für den Monte Carlo Tree
//struct MCTSNode {
//    BlokusGameState gameState;
//    MCTSNode* parent;
//    std::vector<MCTSNode*> children;
//    int visits;
//    double score; // Oder wins / losses
//
//    // Der Zug, der zu diesem Knoten geführt hat (vom Parent aus gesehen)
//    std::vector<int> moveFromParent;
//
//    MCTSNode(const BlokusGameState& state, MCTSNode* p = nullptr, const std::vector<int>& move = {})
//            : gameState(state), parent(p), visits(0), score(0.0), moveFromParent(move) {}
//
//    ~MCTSNode() {
//        for (MCTSNode* child : children) {
//            delete child;
//        }
//    }
//};
//
//// Die Haupt-MCTS-Klasse
//class MonteCarloTreeSearch {
//public:
//    MonteCarloTreeSearch(long timeoutMillis) : timeout_(std::chrono::milliseconds(timeoutMillis)) {}
//
//    std::vector<int> findBestMove(const BlokusGameState& initialGameState) {
//        auto startTime = std::chrono::high_resolution_clock::now();
//        MCTSNode* root = new MCTSNode(initialGameState);
//
//        // MCTS-Loop
//        while (std::chrono::duration_cast<std::chrono::milliseconds>(
//                std::chrono::high_resolution_clock::now() - startTime).count() < timeout_.count()) {
//            MCTSNode* selectedNode = select(root);
//            if (selectedNode->gameState.isGameOver()) {
//                backpropagate(selectedNode, selectedNode->gameState.getWinner());
//                continue;
//            }
//
//            MCTSNode* expandedNode = expand(selectedNode);
//            int simulationResult = simulate(expandedNode->gameState);
//            backpropagate(expandedNode, simulationResult);
//        }
//
//        // Finde den besten Zug basierend auf den Visit-Zahlen oder Scores
//        MCTSNode* bestChild = nullptr;
//        int maxVisits = -1;
//        for (MCTSNode* child : root->children) {
//            if (child->visits > maxVisits) {
//                maxVisits = child->visits;
//                bestChild = child;
//            }
//        }
//
//        std::vector<int> result = {};
//        if (bestChild) {
//            result = bestChild->moveFromParent;
//        }
//
//        delete root; // Speicher freigeben
//        return result;
//    }
//
//private:
//    std::chrono::milliseconds timeout_;
//
//    MCTSNode* select(MCTSNode* node) {
//        // Implementiere UCT (Upper Confidence Bound 1 applied to trees)
//        // Wähle den Kinderknoten, der das höchste UCT-Ergebnis hat
//        // Bis zu einem Blattknoten oder einem unbesuchten Knoten
//        return node; // Platzhalter
//    }
//
//    MCTSNode* expand(MCTSNode* node) {
//        // Erzeuge alle möglichen Kinderknoten für den aktuellen Zustand
//        // Wähle einen zufällig aus, der noch nicht expandiert wurde
//        // Füge ihn zum Baum hinzu
//        return node; // Platzhalter
//    }
//
//    int simulate(BlokusGameState gameState) {
//        // Führe eine zufällige Simulation (Rollout) vom aktuellen Zustand aus
//        // bis das Spiel beendet ist.
//        // Gebe das Ergebnis zurück (z.B. Index des Gewinners, 0 für Unentschieden, etc.)
//        return -1; // Platzhalter
//    }
//
//    void backpropagate(MCTSNode* node, int simulationResult) {
//        // Aktualisiere Besuche und Scores von diesem Knoten bis zur Wurzel
//        // Je nach Spiel (0-1-Win, oder Punktezahl) anpassen
//        while (node != nullptr) {
//            node->visits++;
//            // score += (isWinner(simulationResult, node->gameState.currentPlayer) ? 1.0 : 0.0);
//            node = node->parent;
//        }
//    }
//};
//
//// JNI Funktion (Auszug, Anpassungen für LongArray)
//extern "C" JNIEXPORT jintArray JNICALL
//Java_com_example_blokus2p_ai_BlokusBot_findBestMove(
//        JNIEnv* env,
//        jobject /* this */,
//        jlongArray jgameBoardMasks, // <-- Neu: LongArray für gameBoardMasks
//        jobjectArray jplayerBitBoards, // <-- Neu: ObjectArray von LongArrays für playerBitBoards
//        jobjectArray jpiecesAvailable,
//        jint jcurrentPlayer,
//        jlong jtimeoutMillis) {
//
//    // Konvertierung von jlongArray jgameBoardMasks
//    jsize gameBoardMasksLength = env->GetArrayLength(jgameBoardMasks);
//    jlong* gameBoardMasksElements = env->GetLongArrayElements(jgameBoardMasks, NULL);
//    std::vector<uint64_t> gameBoardMasks(gameBoardMasksElements, gameBoardMasksElements + gameBoardMasksLength);
//    env->ReleaseLongArrayElements(jgameBoardMasks, gameBoardMasksElements, JNI_ABORT);
//
//    // Konvertierung von jobjectArray jplayerBitBoards
//    jsize numPlayers = env->GetArrayLength(jplayerBitBoards);
//    std::vector<std::vector<uint64_t>> playerBitBoards(numPlayers, std::vector<uint64_t>(4)); // Annahme 4 Longs pro Spieler
//    for (int i = 0; i < numPlayers; ++i) {
//        jlongArray jplayerBoard = (jlongArray)env->GetObjectArrayElement(jplayerBitBoards, i);
//        jlong* playerBoardElements = env->GetLongArrayElements(jplayerBoard, NULL);
//        for (int j = 0; j < 4; ++j) { // 4 Longs pro Spieler
//            playerBitBoards[i][j] = static_cast<uint64_t>(playerBoardElements[j]);
//        }
//        env->ReleaseLongArrayElements(jplayerBoard, playerBoardElements, JNI_ABORT);
//        env->DeleteLocalRef(jplayerBoard);
//    }
//    // ... Rest der Konvertierungen und MCTS-Logik
//}
