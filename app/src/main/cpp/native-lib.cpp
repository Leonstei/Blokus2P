//
// Created by leons on 06.01.2026.
//
#include <jni.h>  // Für JNI
#include <android/log.h>
#include "blokus_duo.h"
#include "blokus_duo_logic.h"


struct BlokusStateSerialized {
    // Reihenfolge fest definieren!
    uint32_t current_player;          // 4 Byte (0 oder 1)
    uint32_t outcome;                 // 4 Byte (z. B. 255 = invalid, 0/1 = Gewinner)
    uint32_t num_moves;              // 4 Bytes
    uint8_t player0_pass;            // 1 Byte (bool)
    uint8_t player1_pass;            // 1 Byte (bool)
    uint8_t padding[2];              // Alignment auf 8 Bytes

    uint32_t polyomino_mask_player_0; //4
    uint32_t polyomino_mask_player_1; //4

    uint64_t combined_board[kNumBitboardParts];
    uint64_t player_0_board[kNumBitboardParts];
    uint64_t player_1_board[kNumBitboardParts];
    uint64_t bit_border[kNumBitboardParts];
    uint64_t player_0_edges[kNumBitboardParts];
    uint64_t player_1_edges[kNumBitboardParts];
};

constexpr size_t kBitboardSize = kNumBitboardParts * sizeof(uint64_t);

extern "C" JNIEXPORT jint JNICALL
Java_com_example_blokus2p_model_BlokusNative_doubleNumber(JNIEnv* env, jobject /* this */, jint number) {
    return number * 2;  // Einfache Logik zum Testen
}


extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_initGame(JNIEnv* env, jobject /*this*/) {
    auto game = std::make_shared<BlokusDuoGame>();
    auto initial_state = game->NewInitialState();

    BlokusStateSerialized serialized{};
    serialized.current_player = initial_state.CurrentPlayer();
    serialized.outcome = initial_state.outcome();
    serialized.num_moves = initial_state.num_moves_;
    serialized.player0_pass = initial_state.player0_pass;
    serialized.player1_pass = initial_state.player1_pass;
    serialized.polyomino_mask_player_0 = initial_state.polyomino_mask_player_0;
    serialized.polyomino_mask_player_1 = initial_state.polyomino_mask_player_1;

    std::memcpy(serialized.combined_board, initial_state.combined_board_.data(), kBitboardSize);
    std::memcpy(serialized.player_0_board, initial_state.player_0_board_.data(), kBitboardSize);
    std::memcpy(serialized.player_1_board, initial_state.player_1_board_.data(), kBitboardSize);
    std::memcpy(serialized.bit_border, initial_state.bit_border_.data(), kBitboardSize);
    std::memcpy(serialized.player_0_edges, initial_state.player_0_edges.data(), kBitboardSize);
    std::memcpy(serialized.player_1_edges, initial_state.player_1_edges.data(), kBitboardSize);

    // In ByteArray umwandeln
    size_t totalSize = sizeof(BlokusStateSerialized);
    jbyteArray result = env->NewByteArray(totalSize);
    env->SetByteArrayRegion(result, 0, totalSize, reinterpret_cast<jbyte*>(&serialized));

    return result;
}
extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_getLegalActions(JNIEnv *env, jobject thiz,
                                                             jbyteArray stateBytes, jint player) {
    // TODO: implement getLegalActions()
}




BlokusStateSerialized deserializeState(const uint8_t* data) {
    // Wie zuvor: ByteBuffer → Struct füllen
}

void serializeState(const BlokusDuoState& state, uint8_t* outData) {
    // Wie zuvor: memcpy für alle Felder
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_applyAction(
        JNIEnv* env,
        jobject /*this*/,
        jbyteArray stateBytes,
        jint actionId
) {
    //Todo

//     //Input deserialisieren
//    jsize size = env->GetArrayLength(stateBytes);
//    uint8_t* input = reinterpret_cast<uint8_t*>(env->GetByteArrayElements(stateBytes, nullptr));
//
//    BlokusStateSerialized serializedIn;
//    std::memcpy(&serializedIn, input, sizeof(BlokusStateSerialized));
//
//    // Temporären State rekonstruieren (oder direkt auf serialized arbeiten, wenn möglich)
//    BlokusDuoState state;  // Du musst einen Weg haben, aus serializedIn einen State zu bauen
////    state.combined_board_ = /* copy from serializedIn */;
////    state.player_0_board_ = /* ... */;
//    // ... alle Felder setzen
//    state.SetCurrentPlayer(serializedIn.current_player);
//    // ...
//
//    // Action anwenden – genau wie in deiner OpenSpiel-Impl!
//    const auto& decoded = ALL_DISTINCT_ACTIONS[actionId];
//    state.ApplyAction(actionId);  // Oder manuell mit decoded.bitmask etc.
//
//    // Neuen State serialisieren
//    BlokusStateSerialized serializedOut{};
//    // memcpy aller Felder aus state → serializedOut
//
//    jbyteArray result = env->NewByteArray(sizeof(BlokusStateSerialized));
//    env->SetByteArrayRegion(result, 0, sizeof(BlokusStateSerialized),
//                            reinterpret_cast<jbyte*>(&serializedOut));
//
//    env->ReleaseByteArrayElements(stateBytes, reinterpret_cast<jbyte*>(input), JNI_ABORT);
//    return result;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_blokus2p_model_BlokusNative_isTerminal(JNIEnv *env, jobject thiz,
                                                        jbyteArray stateBytes) {
    // TODO: implement isTerminal()
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_blokus2p_model_BlokusNative_getBestMoveMinimax(JNIEnv *env, jobject thiz,
                                                                jbyteArray stateBytes, jint player,
                                                                jint depth) {
    // TODO: implement getBestMoveMinimax()
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_blokus2p_model_BlokusNative_getBestMoveMcts(JNIEnv *env, jobject thiz,
                                                             jbyteArray stateBytes, jint player,
                                                             jint iterations) {
    // TODO: implement getBestMoveMcts()
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_blokus2p_MainActivity_doubleNumber(JNIEnv *env, jobject thiz, jint number) {
    // TODO: implement doubleNumber()
}
