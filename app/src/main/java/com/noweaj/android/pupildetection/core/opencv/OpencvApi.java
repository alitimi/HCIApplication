package com.noweaj.android.pupildetection.core.opencv;

import com.noweaj.android.pupildetection.data.CascadeData;

import org.opencv.core.Mat;

public class OpencvApi {

    /**
     *
     * @param input
     * @return int[] contains info of detected face in Rect form
     * int[0] -> face x
     * int[1] -> face y
     * int[2] -> face width
     * int[3] -> face height
     */
    public static int[][] detectFrontalFace(Mat input){
        return OpencvNative.DetectFrontalFace(CascadeData.cascade_frontalface, input.getNativeObjAddr());
    }

    public static int[][] detectEyes(Mat input, int[][] detectedFaces){
        return OpencvNative.DetectEyes(CascadeData.cascade_eyes, input.getNativeObjAddr(), detectedFaces);
    }
}
