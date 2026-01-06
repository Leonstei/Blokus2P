package com.example.blokus2p.ai

/*
MCTS mit Zobrist-Hashing + Transposition Table (DAG-fähig)
Kotlin-Beispiel - als Leitstruktur, anpassbar an deine Board-API.

Wichtig: Diese Implementierung ist bewusst allgemein gehalten.
Du solltest sie an die konkrete Board-Repräsentation (mutable vs immutable,
Move-Typ, Hash-Berechnung) in deinem Projekt anpassen.

Kernideen:
- BoardState muss eine stabile 64-bit Zobrist-Hash-Funktion bereitstellen
  (fun zobristHash(): Long).
- MCTS speichert Knoten in einer ConcurrentHashMap<Long, Node> keyed by hash.
- Ein Node enthält atomare Statistiken (visits, wins) und eine Liste von Kanten
  (Move -> childHash). Knoten können mehrere Eltern haben -> DAG.
- Tree-Reuse: Nach jedem gegnerischen Zug verschiebst du den rootHash
  auf den Hash der aktuellen Boardsituation (falls vorhanden).
- Thread-safety: atomare Updates, ConcurrentHashMap. Für höhere Performance
  sind weitergehende Lock-Free-Optimierungen möglich.
*/

//import java.util.concurrent.ConcurrentHashMap
//import java.util.concurrent.ThreadLocalRandom
//import java.util.concurrent.atomic.AtomicInteger
//import java.util.concurrent.atomic.AtomicLong
//import kotlin.math.ln
//import kotlin.math.sqrt
//
//// --- Interfaces, anpassbar ---
//
///**
// * Repräsentiert den Spielzustand. Deine Implementierung muss
// * - stabile 64-bit Zobrist-Hashes liefern
// * - legalMoves generieren
// * - einen Move anwenden und den nächsten BoardState zurückgeben (immutable)
// */
//interface BoardState {
//    fun zobristHash(): Long
//    fun currentPlayer(): Int // z.B. 0 oder 1
//    fun isTerminal(): Boolean
//    fun winner(): Int? // 0,1 or null (bei Unentschieden)
//    fun legalMoves(): List<Move>
//    fun applyMove(move: Move): BoardState
//    // Optional: heuristische Bewertung für rollouts early-stop
//    fun heuristicValueFor(player: Int): Double
//}
//
//data class Move(val id: Int) // ersetze mit sinnvoller Repräsentation
//
//// --- Zobrist Helper (falls du selbst generieren willst) ---
//class Zobrist(val numSquares: Int, val numPlayers: Int = 2, seed: Long = 123456L) {
//    private val rnd = java.util.Random(seed)
//    val table: Array<LongArray> = Array(numSquares) { LongArray(numPlayers) { rnd.nextLong() } }
//    fun hashFor(square: Int, player: Int): Long = table[square][player]
//}
//
//// --- Node / Edge Struktur ---
//
///**
// * Edge verbindet einen Move mit dem Kind-Knoten (childHash). childHash kann
// * bereits in der table existieren (Transposition). `isExpanded` signalisiert,
// * ob diese Kante schon expandiert wurde.
// */
//data class Edge(val move: Move, @Volatile var childHash: Long? = null, @Volatile var isExpanded: Boolean = false)
//
///**
// * NodeStats: atomare Zähler für thread-sichere Updates.
// * wins: gespeicherte Summe von "Gewinnpunkten" aus Sicht eines Nominal-Players
// * visits: Anzahl Backpropagations
// */
//class NodeStats(initialPlayer: Int) {
//    val visits = AtomicLong(0L)
//    val wins = AtomicLong(0L)
//    // optional: store which player this node was evaluated for (root player)
//    val nodePlayer = initialPlayer
//}
//
///**
// * Node: speichert Move-Edges und Stats. Kinder werden über hashes referenziert.
// * Bei Transpositionen existiert nur ein Node pro Hash.
// */
//class Node(val hash: Long, initialPlayer: Int) {
//    val stats = NodeStats(initialPlayer)
//    // Map von Move.id -> Edge
//    val edges: ConcurrentHashMap<Int, Edge> = ConcurrentHashMap()
//    @Volatile var fullyExpanded = false
//}
//
//// --- Transposition Table (Node Repository) ---
//class TranspositionTable {
//    private val table = ConcurrentHashMap<Long, Node>()
//    fun getOrCreate(hash: Long, initialPlayer: Int): Node {
//        return table.computeIfAbsent(hash) { Node(hash, initialPlayer) }
//    }
//    fun get(hash: Long): Node? = table[hash]
//    fun contains(hash: Long): Boolean = table.containsKey(hash)
//}
//
//// --- MCTS Algorithmus mit UCT ---
//class MCTS(val tt: TranspositionTable, val c: Double = 1.414) {
//
//    // Auswahlphase (Selection): wähle rekursiv die beste Kante nach UCT
//    fun select(state: BoardState, rootHash: Long): List<Pair<Long, Edge>> {
//        val path = mutableListOf<Pair<Long, Edge>>()
//        var curHash = rootHash
//        var node = tt.get(curHash) ?: error("root not found in TT")
//
//        while (!state.isTerminal()) {
//            // wenn kein Kinde vorhanden -> Stop (zur Expansion bereit)
//            if (node.edges.isEmpty()) break
//
//            // wähle bestes Kind (UCT)
//            val totalVisits = node.stats.visits.get().toDouble()
//            var bestScore = Double.NEGATIVE_INFINITY
//            var bestEdge: Edge? = null
//            var bestChildHash: Long? = null
//
//            for (edge in node.edges.values) {
//                val childHash = edge.childHash ?: continue
//                val childNode = tt.get(childHash) ?: continue
//                val w = childNode.stats.wins.get().toDouble()
//                val n = childNode.stats.visits.get().toDouble()
//                val uct = if (n == 0.0) Double.POSITIVE_INFINITY
//                else (w / n) + c * sqrt(ln(totalVisits + 1.0) / n)
//                if (uct > bestScore) {
//                    bestScore = uct
//                    bestEdge = edge
//                    bestChildHash = childHash
//                }
//            }
//
//            if (bestEdge == null || bestChildHash == null) break
//            path.add(curHash to bestEdge)
//            // advance state
//            val nextState = state.applyMove(bestEdge.move)
//            // update for next loop
//            curHash = bestChildHash
//            node = tt.get(curHash) ?: break
//            // mutate state reference for selection loop
//            // NOTE: to avoid changing original state, callers should pass a mutable copy
//            // but for simplicity we just reassign 'state' by shadowing - here we need
//            // a var; we'll instead return path and let caller manage states separately.
//            return path // selection returns first step: caller will manage iterative selection
//        }
//        return path
//    }
//
//    // Erweiterung (Expansion)
//    fun expand(state: BoardState, nodeHash: Long): Long {
//        val node = tt.get(nodeHash) ?: error("node not found")
//        val moves = state.legalMoves()
//        // Füge alle legalen Moves hinzu als Edges (falls noch nicht vorhanden)
//        for (m in moves) {
//            node.edges.computeIfAbsent(m.id) { Edge(m) }
//        }
//        // Wähle eine nicht expandierte Kante zufällig zum Expandieren
//        val unexpanded = node.edges.values.filter { !it.isExpanded }
//        if (unexpanded.isEmpty()) {
//            node.fullyExpanded = true
//            // keine neuen Kanten -> return eigener Hash
//            return nodeHash
//        }
//        val chosen = unexpanded[ThreadLocalRandom.current().nextInt(unexpanded.size)]
//        // apply move to get child state & hash
//        val childState = state.applyMove(chosen.move)
//        val childHash = childState.zobristHash()
//
//        // Falls TT schon einen Node für childHash hat, verknüpfe nur die Edge
//        val childNode = tt.getOrCreate(childHash, childState.currentPlayer())
//        chosen.childHash = childHash
//        chosen.isExpanded = true
//        return childHash
//    }
//
//    // Simulation / Rollout (spielerisch einfach gehalten)
//    fun rollout(state: BoardState, maxRolloutSteps: Int = 200): Double {
//        var s = state
//        var steps = 0
//        while (!s.isTerminal() && steps++ < maxRolloutSteps) {
//            val moves = s.legalMoves()
//            if (moves.isEmpty()) break
//            // heuristisch geführte Auswahl: z.B. gelegentlich greedy
//            val pick = if (ThreadLocalRandom.current().nextDouble() < 0.2) {
//                // greedy heuristik: wähle Move mit besten heuristischen value after apply
//                moves.maxByOrNull { m ->
//                    val ns = s.applyMove(m)
//                    ns.heuristicValueFor(s.currentPlayer())
//                } ?: moves.random()
//            } else moves.random()
//            s = s.applyMove(pick)
//        }
//        // Rückgabe: Reward aus Sicht des rootPlayers (0 oder 1)
//        val winner = s.winner()
//        return when (winner) {
//            null -> 0.5
//            else -> if (winner == state.currentPlayer()) 1.0 else 0.0
//        }
//    }
//
//    // Backpropagation: update aller Knoten auf dem Pfad
//    fun backpropagate(pathHashes: List<Long>, reward: Double) {
//        for (h in pathHashes) {
//            val n = tt.get(h) ?: continue
//            n.stats.visits.getAndIncrement()
//            // wins speichert Summe der reward für einen 'nominal root player'
//            n.stats.wins.getAndAdd((reward * 1000).toLong()) // skaliert als Beispiel
//        }
//    }
//
//    // High-level single MCTS-Iteration (nicht parallelisiert)
//    fun iteration(rootState: BoardState, rootHash: Long) {
//        // 1) Selection
//        var stateCursor: BoardState = rootState
//        var curHash = rootHash
//        val path = mutableListOf<Long>()
//        while (true) {
//            val node = tt.getOrCreate(curHash, stateCursor.currentPlayer())
//            path.add(curHash)
//            // wenn terminal -> stop
//            if (stateCursor.isTerminal()) break
//            // expand if node is not expanded
//            if (!node.fullyExpanded) {
//                val childHash = expand(stateCursor, curHash)
//                // add childHash to path, rollout from child
//                val childState = stateCursor.applyMove(node.edges.values.first { it.childHash == childHash }.move)
//                path.add(childHash)
//                val reward = rollout(childState)
//                backpropagate(path, reward)
//                return
//            }
//            // sonst wähle besten Kind via UCT
//            // simple selection: zufällige Auswahl falls keine visits
//            val totalVisits = node.stats.visits.get().toDouble()
//            var bestEdge: Edge? = null
//            var bestChildHash: Long? = null
//            var bestScore = Double.NEGATIVE_INFINITY
//            for (edge in node.edges.values) {
//                val ch = edge.childHash ?: continue
//                val child = tt.getOrCreate(ch, stateCursor.currentPlayer())
//                val n = child.stats.visits.get().toDouble()
//                val w = child.stats.wins.get().toDouble()
//                val uct = if (n == 0.0) Double.POSITIVE_INFINITY
//                else (w / n) + c * sqrt(ln(totalVisits + 1.0) / n)
//                if (uct > bestScore) {
//                    bestScore = uct
//                    bestEdge = edge
//                    bestChildHash = ch
//                }
//            }
//            if (bestEdge == null || bestChildHash == null) break
//            // advance
//            stateCursor = stateCursor.applyMove(bestEdge.move)
//            curHash = bestChildHash
//        }
//        // Falls wir aus der Schleife rauskommen ohne zu expandieren -> rollout vom aktuellen state
//        val reward = rollout(stateCursor)
//        backpropagate(path, reward)
//    }
//
//    // Public API: führe n Iterationen durch
//    fun runIterations(rootState: BoardState, rootHash: Long, iterations: Int) {
//        // ensure root exists
//        tt.getOrCreate(rootHash, rootState.currentPlayer())
//        for (i in 0 until iterations) {
//            iteration(rootState, rootHash)
//        }
//    }
//
//    // Wähle den besten Move nach Visits
//    fun bestMove(rootHash: Long): Move? {
//        val root = tt.get(rootHash) ?: return null
//        val best = root.edges.values.maxByOrNull { e ->
//            val ch = e.childHash ?: return@maxByOrNull -1L
//            val child = tt.get(ch) ?: return@maxByOrNull -1L
//            child.stats.visits.get()
//        }
//        return best?.move
//    }
//}
//
//// --- Tree Reuse (Root Shifting) ---
//
///**
// * Verschiebt den Root auf den aktuellen Board-Hash, falls vorhanden in der TT.
// * Alle bisherigen Statistiken bleiben erhalten.
// */
//fun shiftRootTo(tt: TranspositionTable, newRootHash: Long): Long {
//    return if (tt.contains(newRootHash)) newRootHash else -1L
//}

/*
Usage-Pattern:

val tt = TranspositionTable()
val mcts = MCTS(tt)
val rootState: BoardState = ... // deine Implementierung
val rootHash = rootState.zobristHash()
tt.getOrCreate(rootHash, rootState.currentPlayer())

// run iterations
mcts.runIterations(rootState, rootHash, 1000)
val move = mcts.bestMove(rootHash)

// nach gegnerzug:
val newRootState = currentBoardAfterOpponentMove
val newHash = newRootState.zobristHash()
if (shiftRootTo(tt, newHash) >= 0) {
    // reuse tree: set rootHash := newHash
} else {
    // clear or create new root
}

Anpassungen, die du wahrscheinlich machen willst:
- bessere Rollout-Policy (domain heuristics)
- Virtual Loss / Thread-safety für parallele Iterationen
- genauere reward-Skalierung (statt wins als Long-Scaling)
- deterministische Move-IDs und bessere Move-Hashing
- optional: collision check: wenn hash matched, vergleiche Board-Inhalt zum 100%igem Match
*/
