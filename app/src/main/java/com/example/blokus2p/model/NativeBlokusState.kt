package com.example.blokus2p.model

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class NativeBlokusState(
    val currentPlayer: Int,
    val outcome: Int, // -1 = läuft, 0/1 = Gewinner
    val numMoves:  Int,
    val player0Pass: Boolean,
    val player1Pass: Boolean,
    val polyominoMaskP0: UInt,
    val polyominoMaskP1: UInt,
    val combinedBoard: LongArray,     // 4 Longs
    val player0Board: LongArray,      // 4 Longs
    val player1Board: LongArray,      // 4 Longs
    val border: LongArray,            // 4 Longs
    val player0Edges: LongArray,      // 4 Longs
    val player1Edges: LongArray     // 4 Longs
)
fun ByteArray.toNativeBlokusState(): NativeBlokusState {
        val buffer = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)

        val currentPlayer = buffer.int
        val outcome = buffer.int
        val numMoves = buffer.int
        val p0Pass = buffer.get() != 0.toByte()
        val p1Pass = buffer.get() != 0.toByte()
        buffer.position(buffer.position() + 2)

        val maskP0 = buffer.int.toUInt()
        val maskP1 = buffer.int.toUInt()

        val combined = LongArray(4) { buffer.long }
        val p0Board = LongArray(4) { buffer.long }
        val p1Board = LongArray(4) { buffer.long }
        val border = LongArray(4) { buffer.long }
        val p0Edges = LongArray(4) { buffer.long }
        val p1Edges = LongArray(4) { buffer.long }

        return NativeBlokusState(
            currentPlayer = currentPlayer,
            outcome = outcome,
            numMoves =numMoves,
            polyominoMaskP0 = maskP0,
            polyominoMaskP1 = maskP1,
            combinedBoard = combined,
            player0Board = p0Board,
            player1Board = p1Board,
            border = border,
            player0Edges = p0Edges,
            player1Edges = p1Edges,
            player0Pass = p0Pass,
            player1Pass = p1Pass
        )
    }

