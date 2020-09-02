//
// Created by Noweaj.dev on 2020-07-21.
//

#include <jni.h>
#include "com_noweaj_android_pupildetection_core_opencv_OpencvNative.h"

#include <opencv2/opencv.hpp>

#include <android/log.h>
#include <algorithm>

using namespace cv;
using namespace std;

extern "C"{

    bool compare(Point a, Point b){
        return a.y < b.y;
    }

    void getValidEyes(std::vector<Rect> eyes, std::vector<Point>& valid_eyes, int bounds, double face_x, double face_y){

        std::vector<Point> eyesInPoint;
        for(int j=0; j<eyes.size(); j++){
            Point eye_center(face_x+eyes[j].x+eyes[j].width/2, face_y+eyes[j].y+eyes[j].height/2);
            eyesInPoint.push_back(eye_center);
        }

        // sort
        std::sort(eyesInPoint.begin(), eyesInPoint.end(), [](Point a, Point b){return a.y < b.y;});

        // get reference value for
        int ref = eyesInPoint[eyesInPoint.size()/2].y / bounds;

        int diff;
        for(int i=1; i<eyesInPoint.size(); i++){
            diff = abs(eyesInPoint[i].y - eyesInPoint[i-1].y);
            if(diff < ref && valid_eyes.size() < 2){
                valid_eyes.push_back(eyesInPoint[i-1]);
                valid_eyes.push_back(eyesInPoint[i]);
            }
        }
    }

    double face_x_global, face_y_global;

    void getValidEyes_rect(vector<Rect> eyes, vector<Rect>& valid_eyes, int bounds, double face_x, double face_y){

//        face_x_global = face_x;
        face_y_global = face_y;

        sort(eyes.begin(), eyes.end(), [](Rect a, Rect b){
//            Point eye_a(face_x_global+a.x+a.width/2, face_y_global+a.y+a.height/2);
//            Point eye_b(face_x_global+b.x+b.width/2, face_y_global+b.y+b.height/2);
            return face_y_global+a.y+a.height/2 < face_y_global+b.y+b.height/2;
        });

        Rect refEye = eyes[eyes.size()/2];
        int ref = (face_y+refEye.y+refEye.height/2) / bounds;
        __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "ref: %d", ref);

        int diff;
        for(int i=1; i<eyes.size(); i++){
            int eye_a = face_y+eyes[i].y+eyes[i].height/2;
            int eye_b = face_y+eyes[i-1].y+eyes[i-1].height/2;
            diff = abs(eye_a - eye_b);
            __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "diff: %d", diff);
            if(diff < ref && valid_eyes.size() < 2){
                valid_eyes.push_back(eyes[i-1]);
                valid_eyes.push_back(eyes[i]);
            }
        }
    }

    JNIEXPORT
    void JNICALL Java_com_noweaj_android_pupildetection_core_opencv_OpencvNative_ConvertRGBtoGray
    (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult){
//        Mat &matInput = *(Mat *)matAddrInput;
//        Mat &matResult = *(Mat *)matAddrResult;
        cvtColor(*(Mat *)matAddrInput, *(Mat *)matAddrResult, COLOR_RGBA2GRAY);
    }

    JNIEXPORT
    jlong JNICALL Java_com_noweaj_android_pupildetection_core_opencv_OpencvNative_LoadCascade
    (JNIEnv *env, jobject instance, jstring filePath){
        const char *filePathString = env->GetStringUTFChars(filePath, 0);

        jlong ret = 0;
        ret = (jlong) new CascadeClassifier(filePathString);
        if(((CascadeClassifier *) ret) -> empty()){
            __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "CascasdeClassifier loading failed %s", filePathString);
            ret = 0;
        } else {
            __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "CascasdeClassifier loading successful %s", filePathString);
        }

        env->ReleaseStringUTFChars(filePath, filePathString);
        return ret;
    }

    JNIEXPORT jobjectArray JNICALL Java_com_noweaj_android_pupildetection_core_opencv_OpencvNative_DetectFrontalFace
    (JNIEnv *env, jobject instance, jlong cascade_face, jlong matAddrInput){

        Mat &img_result = *(Mat *)matAddrInput;

        // draw face guide
        ellipse(img_result, Point(img_result.cols/2, img_result.rows/2), Size(img_result.cols/3, img_result.rows/1.5), 0, 0, 360, Scalar(0, 255, 255), 10, 8, 0);

        // Detect face
        Mat img_gray;
        cvtColor(img_result, img_gray, COLOR_RGBA2GRAY);

        vector<Rect> faces;

        int minWidth = img_result.cols/3;

        ((CascadeClassifier *) cascade_face)->detectMultiScale(img_gray, faces, 1.1, 2, 0|CASCADE_SCALE_IMAGE, Size(minWidth, 30));
//        __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "face %d found", faces.size());

        // generate return object
        jclass facesArrayClass = env->FindClass("[[I");
        jclass faceInfoClass = env->FindClass("[I");
        jobjectArray facesArray = env->NewObjectArray(faces.size(), faceInfoClass, NULL);

        for(int i=0; i<faces.size(); i++){
            int face_width = faces[i].width;
            int face_height = faces[i].height;

            int face_x = faces[i].x;
            int face_y = faces[i].y;

            Point center(face_x+face_width/2, face_y+face_height/2);
            ellipse(img_result, center, Size(face_width/2, face_height/2), 0, 0, 360, Scalar(255, 0, 255), 20, 8, 0);

            jintArray faceInfo = env->NewIntArray(4);
            jint *buf = new jint[4];
            buf[0] = face_x;
            buf[1] = face_y;
            buf[2] = face_width;
            buf[3] = face_height;
            env->SetIntArrayRegion(faceInfo, i*4, 4, buf);
            env->SetObjectArrayElement(facesArray, i, faceInfo);
            delete[] buf;
        }
        return facesArray;
    }

    JNIEXPORT jobjectArray JNICALL Java_com_noweaj_android_pupildetection_core_opencv_OpencvNative_DetectEyes
    (JNIEnv *env, jobject instance, jlong cascade_eye, jlong matAddrInput, jobjectArray detectedFaces){

    }

    JNIEXPORT void JNICALL Java_com_noweaj_android_pupildetection_core_opencv_OpencvNative_DetectPupil
    (JNIEnv *env, jobject instance, jlong cascade_face, jlong cascade_eye, jlong matAddrInput, jlong matAddrResult){

        Mat &img_result = *(Mat *)matAddrResult;

        // draw face guide
//        circle(img_result, Point(0, 0), 10, Scalar(255, 0, 255), 20, 8, 0);
//        circle(img_result, Point(img_result.cols, 0), 10, Scalar(255, 0, 255), 20, 8, 0);
//        circle(img_result, Point(0, img_result.rows), 10, Scalar(255, 0, 255), 20, 8, 0);
//        circle(img_result, Point(img_result.cols, img_result.rows), 10, Scalar(255, 0, 255), 20, 8, 0);
        ellipse(img_result, Point(img_result.cols/2, img_result.rows/2), Size(img_result.cols/3, img_result.rows/1.5), 0, 0, 360, Scalar(0, 255, 255), 10, 8, 0);

        // Detect face
        Mat img_gray;
        cvtColor(img_result, img_gray, COLOR_RGBA2GRAY);

        vector<Rect> faces;

        int minWidth = img_result.cols/3;

        ((CascadeClassifier *) cascade_face)->detectMultiScale(img_gray, faces, 1.1, 2, 0|CASCADE_SCALE_IMAGE, Size(minWidth, 30));
//        __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "face %d found", faces.size());

        for(int i=0; i<faces.size(); i++){
            double face_width = faces[i].width;
            double face_height = faces[i].height;

            double face_x = faces[i].x;
            double face_y = faces[i].y;

            Point center(face_x+face_width/2, face_y+face_height/2);
            ellipse(img_result, center, Size(face_width/2, face_height/2), 0, 0, 360, Scalar(255, 0, 255), 20, 8, 0);

            // Detect eyes
            Rect face_area(face_x, face_y, face_width, face_height);
            Mat faceROI = img_gray(face_area);
            vector<Rect> eyes;

            Point face_center(face_x+((face_width - face_x)/2), face_y+((face_height - face_y)/2));

            ((CascadeClassifier *) cascade_eye)->detectMultiScale(faceROI, eyes, 1.1, 2, 0|CASCADE_SCALE_IMAGE, Size(30, 30));
            __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "============================");

            // get valid eyes
            vector<Rect> valid_eyes;
            getValidEyes_rect(eyes, valid_eyes, 20, face_x, face_y);
            __android_log_print(ANDROID_LOG_DEBUG, "native-lib ::: ", "valid_eyes size: %d", valid_eyes.size());

            for(int j=0; j<valid_eyes.size(); j++){
                Point eye_center(face_x+valid_eyes[j].x+valid_eyes[j].width/2, face_y+valid_eyes[j].y+valid_eyes[j].height/2);
                __android_log_print(ANDROID_LOG_DEBUG, "native-lib ::: ", "eye point: %d %d", eye_center.x, eye_center.y);
//                Point eye_center(face_x+eyes[j].x+eyes[j].width/2, face_y+eyes[j].y+eyes[j].height/2);
//                __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "eye point: %lf %lf", face_x+eyes[j].x+eyes[j].width/2, face_y+eyes[j].y+eyes[j].height/2);
                if(eye_center.y < face_center.y) {
//                    int radius = cvRound((valid_eyes[j].width + valid_eyes[j].height) * 0.25);
//                    circle(img_result, eye_center, radius, Scalar(255, 0, 255), 20, 8, 0);
                    Rect valid_eyes_corrected(face_x+valid_eyes[j].x, face_y+valid_eyes[j].y, valid_eyes[j].width, valid_eyes[j].height);
//                    rectangle(img_result, valid_eyes_corrected, Scalar(0, 255, 0), 10, 8, 0);
//                    circle(img_result, Point(0, 0), radius, Scalar(255, 0, 255), 20, 8, 0);

                    // get minMaxLoc
                    Rect valid_eyes_without_eyebrows(valid_eyes_corrected.x, valid_eyes_corrected.y+valid_eyes_corrected.height/3, valid_eyes_corrected.width, valid_eyes_corrected.height/3*2);
                    rectangle(img_result, valid_eyes_without_eyebrows, Scalar(0, 255, 0), 10, 8, 0);

                    Mat eyeMat = img_gray(valid_eyes_without_eyebrows);
                    double maxVal, minVal;
                    Point minLoc, maxLoc;
                    minMaxLoc(eyeMat, &minVal, &maxVal, &minLoc, &maxLoc);

                    Point pupil(minLoc.x+valid_eyes_corrected.x, minLoc.y+valid_eyes_without_eyebrows.y);
                    circle(img_result, pupil, 10, Scalar(255, 0, 0), 4, 8, 0);

                    // update pupil list
                }
            }
        }
    }
}
