//
// Created by leons on 06.01.2026.
//
#include <jni.h>  // Für JNI
#include <android/log.h>
#include "blokus_duo.h"
#include "blokus_duo_logic.h"
#include "mcts.h"


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
// In deiner .cpp-Datei (z.B. native-lib.cpp oder blokus_duo.cc)

constexpr size_t totalSize = 216;

void serializeState(const BlokusDuoState& state, uint8_t* outData) {
    uint8_t* ptr = outData;  // Zeiger, der durch das Array wandert

    // 1. Einfache Felder (in exakt derselben Reihenfolge wie in Kotlin!)
    std::memcpy(ptr, &state.current_player_, sizeof(state.current_player_));
    ptr += sizeof(state.current_player_);

    std::memcpy(ptr, &state.outcome_, sizeof(state.outcome_));
    ptr += sizeof(state.outcome_);

    std::memcpy(ptr, &state.num_moves_, sizeof(state.num_moves_));
    ptr += sizeof(state.num_moves_);

    // Bools als uint8_t speichern
    uint8_t p0_pass = state.player0_pass ? 1 : 0;
    uint8_t p1_pass = state.player1_pass ? 1 : 0;

    std::memcpy(ptr, &p0_pass, 1);
    ptr += 1;
    std::memcpy(ptr, &p1_pass, 1);
    ptr += 1;

    std::memset(ptr, 0, 2);
    ptr += 2;

    std::memcpy(ptr, &state.polyomino_mask_player_0, sizeof(state.polyomino_mask_player_0));
    ptr += sizeof(state.polyomino_mask_player_0);

    std::memcpy(ptr, &state.polyomino_mask_player_1, sizeof(state.polyomino_mask_player_1));
    ptr += sizeof(state.polyomino_mask_player_1);

    std::memcpy(ptr, state.combined_board_.data(), kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(ptr, state.player_0_board_.data(), kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(ptr, state.player_1_board_.data(), kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(ptr, state.bit_border_.data(), kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(ptr, state.player_0_edges.data(), kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(ptr, state.player_1_edges.data(), kBitboardSize);
    // ptr += boardSize; // nicht nötig – Ende erreicht
}

BlokusDuoState deserializeState(const uint8_t* data) {
    BlokusDuoState state;
    const uint8_t* ptr = data;

    std::memcpy(&state.current_player_, ptr, sizeof(state.current_player_));
    ptr += sizeof(state.current_player_);

    std::memcpy(&state.outcome_, ptr, sizeof(state.outcome_));
    ptr += sizeof(state.outcome_);

    std::memcpy(&state.num_moves_, ptr, sizeof(state.num_moves_));
    ptr += sizeof(state.num_moves_);

    uint8_t p0_pass_byte, p1_pass_byte;
    std::memcpy(&p0_pass_byte, ptr, 1);
    ptr += 1;
    std::memcpy(&p1_pass_byte, ptr, 1);
    ptr += 1;
    state.player0_pass = (p0_pass_byte != 0);
    state.player1_pass = (p1_pass_byte != 0);

    ptr += 2;  // Padding überspringen

    std::memcpy(&state.polyomino_mask_player_0, ptr, sizeof(state.polyomino_mask_player_0));
    ptr += sizeof(state.polyomino_mask_player_0);

    std::memcpy(&state.polyomino_mask_player_1, ptr, sizeof(state.polyomino_mask_player_1));
    ptr += sizeof(state.polyomino_mask_player_1);


    std::memcpy(state.combined_board_.data(), ptr, kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(state.player_0_board_.data(), ptr, kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(state.player_1_board_.data(), ptr, kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(state.bit_border_.data(), ptr, kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(state.player_0_edges.data(), ptr, kBitboardSize);
    ptr += kBitboardSize;

    std::memcpy(state.player_1_edges.data(), ptr, kBitboardSize);

    return state;
}




extern "C" JNIEXPORT jint JNICALL
Java_com_example_blokus2p_model_BlokusNative_doubleNumber(JNIEnv* env, jobject /* this */, jint number) {
    return number * 2;  // Einfache Logik zum Testen
}


extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_initGame(JNIEnv* env, jobject /*this*/) {
    auto game = std::make_shared<BlokusDuoGame>();
    auto initial_state = game->NewInitialState();

    jbyteArray result = env->NewByteArray(totalSize);
    jbyte* buffer = env->GetByteArrayElements(result, nullptr);

    serializeState(initial_state, reinterpret_cast<uint8_t*>(buffer));

    env->ReleaseByteArrayElements(result, buffer, 0);
    return result;
}
extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_getLegalActions(JNIEnv *env, jobject thiz,
                                                             jbyteArray stateBytes, jint player) {
    jbyte* input = env->GetByteArrayElements(stateBytes, nullptr);

    BlokusDuoState state = deserializeState(reinterpret_cast<const uint8_t*>(input));

    env->ReleaseByteArrayElements(stateBytes, input, JNI_ABORT);

    std::vector<int> legal = state.LegalActions();

    jintArray result = env->NewIntArray(legal.size());
    env->SetIntArrayRegion(result, 0, legal.size(), reinterpret_cast<jint*>(legal.data()));

    return result;
}



extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_blokus2p_model_BlokusNative_applyAction(
        JNIEnv* env,
        jobject /*this*/,
        jbyteArray stateBytes,
        jint actionId
) {
    jsize size = env->GetArrayLength(stateBytes);
    jbyte* input = env->GetByteArrayElements(stateBytes, nullptr);

    BlokusDuoState state = deserializeState(reinterpret_cast<const uint8_t*>(input));

    env->ReleaseByteArrayElements(stateBytes, input, JNI_ABORT);

    // Zug anwenden
    state.ApplyAction(actionId);

    jbyteArray result = env->NewByteArray(size);
    jbyte* output = env->GetByteArrayElements(result, nullptr);

    serializeState(state, reinterpret_cast<uint8_t*>(output));

    env->ReleaseByteArrayElements(result, output, 0);
    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_blokus2p_model_BlokusNative_isTerminal(JNIEnv *env, jobject thiz,
                                                        jbyteArray stateBytes) {
    jsize size = env->GetArrayLength(stateBytes);
    jbyte* input = env->GetByteArrayElements(stateBytes, nullptr);

    BlokusDuoState state = deserializeState(reinterpret_cast<const uint8_t*>(input));
    bool terminal = state.IsTerminal();

    return terminal ? JNI_TRUE : JNI_FALSE;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_blokus2p_model_BlokusNative_getMctsMove(JNIEnv *env, jobject thiz,
                                                         jbyteArray state_bytes) {
    jbyte* input = env->GetByteArrayElements(stateBytes, nullptr);
    BlokusDuoState state = deserializeState(reinterpret_cast<const uint8_t*>(input));
    env->ReleaseByteArrayElements(stateBytes, input, JNI_ABORT);

    MCTSBot bot(1.414, 100, 42);  // uct_c, simulations, seed
    int best_action = bot.Step(state);

    return best_action;
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
