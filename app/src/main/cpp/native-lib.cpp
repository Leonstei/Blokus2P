//
// Created by leons on 06.01.2026.
//
#include <jni.h>  // Für JNI

extern "C" JNIEXPORT jint JNICALL
Java_com_example_blokus2p_model_BlokusNative_doubleNumber(JNIEnv* env, jobject /* this */, jint number) {
    return number * 2;  // Einfache Logik zum Testen
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_blokus2p_model_BlokusNative_initGame(JNIEnv *env, jobject thiz) {
    // TODO: implement initGame()
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_blokus2p_model_BlokusNative_getLegalActions(JNIEnv *env, jobject thiz,
                                                             jstring state_json, jint player) {
    // TODO: implement getLegalActions()
}

// Dein bestehendes ALL_DISTINCT_ACTIONS bleibt global/const wie bisher
extern const std::array<DecodedAction, kNumDistinctActions> ALL_DISTINCT_ACTIONS;

// Deserialisierungs- und Serialisierungsstruktur wie zuvor
struct BlokusStateSerialized { /* wie im letzten Beispiel */ };

BlokusStateSerialized deserializeState(const uint8_t* data) {
    // Wie zuvor: ByteBuffer → Struct füllen
}

void serializeState(const BlokusDuoState& state, uint8_t* outData) {
    // Wie zuvor: memcpy für alle Felder
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_blokus2p_BlokusNative_applyAction(
        JNIEnv* env,
        jobject /*this*/,
        jbyteArray stateBytes,
        jint actionId
) {
    // Input deserialisieren
    jsize size = env->GetArrayLength(stateBytes);
    uint8_t* input = reinterpret_cast<uint8_t*>(env->GetByteArrayElements(stateBytes, nullptr));

    BlokusStateSerialized serializedIn;
    std::memcpy(&serializedIn, input, sizeof(BlokusStateSerialized));

    // Temporären State rekonstruieren (oder direkt auf serialized arbeiten, wenn möglich)
    BlokusDuoState state(nullptr);  // Du musst einen Weg haben, aus serializedIn einen State zu bauen
    state.combined_board_ = /* copy from serializedIn */;
    state.player_0_board_ = /* ... */;
    // ... alle Felder setzen
    state.SetCurrentPlayer(serializedIn.current_player);
    // ...

    // Action anwenden – genau wie in deiner OpenSpiel-Impl!
    const auto& decoded = ALL_DISTINCT_ACTIONS[actionId];
    state.DoApplyAction(actionId);  // Oder manuell mit decoded.bitmask etc.

    // Neuen State serialisieren
    BlokusStateSerialized serializedOut{};
    // memcpy aller Felder aus state → serializedOut

    jbyteArray result = env->NewByteArray(sizeof(BlokusStateSerialized));
    env->SetByteArrayRegion(result, 0, sizeof(BlokusStateSerialized),
                            reinterpret_cast<jbyte*>(&serializedOut));

    env->ReleaseByteArrayElements(stateBytes, reinterpret_cast<jbyte*>(input), JNI_ABORT);
    return result;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_blokus2p_model_BlokusNative_isTerminal(JNIEnv *env, jobject thiz,
                                                        jstring state_json) {
    // TODO: implement isTerminal()
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_blokus2p_model_BlokusNative_getBestMoveMinimax(JNIEnv *env, jobject thiz,
                                                                jstring state_json, jint player,
                                                                jint depth) {
    // TODO: implement getBestMoveMinimax()
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_blokus2p_model_BlokusNative_getBestMoveMcts(JNIEnv *env, jobject thiz,
                                                             jstring state_json, jint player,
                                                             jint iterations) {
    // TODO: implement getBestMoveMcts()
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_blokus2p_MainActivity_doubleNumber(JNIEnv *env, jobject thiz, jint number) {
    // TODO: implement doubleNumber()
}