package com.example.blokus2p.helper

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
