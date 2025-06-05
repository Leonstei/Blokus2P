package com.example.blokus2p.helper

import android.util.Log
import kotlin.time.measureTime

/**
 * Setzt das Bit im Bitboard auf 1 für den gegebenen linearen Index.
 */
fun setBit(bitBoard: LongArray, index: Int) {
    val word = index ushr 6       // index / 64
    val bit = index and 63        // index % 64
    bitBoard[word] = bitBoard[word] or (1L shl bit)
}

/**
 * Löscht das Bit (setzt auf 0) für den gegebenen Index.
 */
fun clearBit(bitBoard: LongArray, index: Int) {
    val word = index ushr 6
    val bit = index and 63
    bitBoard[word] = bitBoard[word] and (1L shl bit).inv()
}

/**
 * Prüft, ob ein Bit auf 1 gesetzt ist.
 */
fun isBitSet(bitBoard: LongArray, index: Int): Boolean {
    val word = index ushr 6
    val bit = index and 63
    return (bitBoard[word] and (1L shl bit)) != 0L
}

/**
 * Führt eine OR-Operation zwischen zwei BitBoards durch (in-place).
 */
fun orBitBoard(dest: LongArray, source: LongArray) {
    for (i in dest.indices) {
        dest[i] = dest[i] or source[i]
    }
}
/**
 * Führt eine OR-Operation zwischen zwei BitBoards durch und gibt ein neues BitBoard zurück.
 * Das ursprüngliche BitBoard bleibt unverändert.
 */
fun orBitBoardReturn(a: LongArray, b: LongArray): LongArray {
    return LongArray(a.size) { i -> a[i] or b[i] }
}

/**
 * Führt eine AND-Operation zwischen zwei BitBoards durch und gibt ein neues BitBoard zurück.
 */
fun andBitBoard(a: LongArray, b: LongArray): LongArray {
    return LongArray(a.size) { i -> a[i] and b[i] }
}

/**
 * Gibt das invertierte BitBoard eines gegebenen BitBoards zurück.
 * Alle gesetzten Bits werden gelöscht und umgekehrt.
 */
fun invBitBoard(board: LongArray): LongArray {
    return LongArray(board.size) { i -> board[i].inv() }
}

/**
 * Prüft, ob zwei BitBoards überlappende Bits haben.
 */
fun hasOverlap(a: LongArray, b: LongArray): Boolean {
    for (i in a.indices) {
        if ((a[i] and b[i]) != 0L) return true
    }
    return false
}

/**
 * Setzt alle Bits im BitBoard auf 0.
 */
fun clearAll(bitBoard: LongArray) {
    for (i in bitBoard.indices) {
        bitBoard[i] = 0L
    }
}

/**
 * Gibt das aktualisierte BitBoard eines Spielers zurück,
 * basierend auf den Änderungen zwischen dem alten und neuen Spielfeld.
 *
 * @param oldBoard Das vorherige Spielfeld (BitBoard)
 * @param newBoard Das neue Spielfeld (BitBoard)
 * @param playerBoard Das BitBoard des Spielers
 * @return Ein neues BitBoard mit den aktualisierten Bits
 */
fun getUpdatedPlayerBitBoard(oldBoard: LongArray, newBoard: LongArray,playerBoard:LongArray): LongArray {
    // Finde alle Bits, die in newBoard gesetzt sind, aber in oldBoard nicht
    val newBits = andBitBoard(newBoard, invBitBoard(oldBoard))
    // Füge die neuen Bits dem alten BitBoard hinzu (OR-Operation)
    return orBitBoardReturn(playerBoard, newBits)
}

fun visualizeBitBoard(bitBoard: LongArray) {
    val bits = bitBoard
        .flatMap { long ->
            (63 downTo 0).map { i ->
                if ((long ushr i) and 1L == 1L) '█' else '.'
            }
        }

    for (y in 0 until 14) {
        for (x in 0 until 14) {
            val index = y * 14 + x
            print(bits[index])
        }
        println()
    }
}
