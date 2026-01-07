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
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_blokus2p_model_BlokusNative_applyAction(JNIEnv *env, jobject thiz,
                                                         jstring state_json, jlong action_id,
                                                         jint player) {
    // TODO: implement applyAction()
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