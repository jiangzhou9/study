//
// Created by 杨强 on 2017/2/27.
//
#include <com_example_yq_ndkdemo_JNIDemo.h>
#include <jni.h>

JNIEXPORT jstring JNICALL Java_com_example_yq_ndkdemo_JNIDemo_getHelloWorldText
  (JNIEnv * env, jobject obj) {
    jclass clazz = env->FindClass("com/example/yq/ndkdemo/MainActivity");
    jmethodID id = env->GetStaticMethodID(clazz, "showToast", "()V");
    env->CallStaticVoidMethod(clazz, id);
    return env->NewStringUTF("hello world from jni");
}
