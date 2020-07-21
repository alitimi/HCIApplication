//
// Created by Noweaj.dev on 2020-07-21.
//

#include <jni.h>
#include "com_noweaj_android_pupildetection_main_MainActivity.h"

#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"{

    JNIEXPORT void JNICALL Java_com_noweaj_android_pupildetection_main_MainActivity_ConvertRGBtoGray(JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult){
//        Mat &matInput = *(Mat *)matAddrInput;
//        Mat &matResult = *(Mat *)matAddrResult;
        cvtColor(*(Mat *)matAddrInput, *(Mat *)matAddrResult, COLOR_RGBA2GRAY);
    }
}
