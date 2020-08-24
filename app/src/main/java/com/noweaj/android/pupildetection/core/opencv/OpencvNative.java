package com.noweaj.android.pupildetection.core.opencv;

public class OpencvNative {
    public OpencvNative(){}
    // Native
    public static native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public static native long LoadCascade(String cascadeFileName);

    public static native void DetectFrontalFace(long cascade_face, long matAddrInput, long matAddrResult);
    public static native void DetectEyes(long cascade_eye, long matAddrInput, long matAddrResult);

    public static native void DetectPupil(long cascade_face, long cascade_eye, long matAddrInput, long matAddrResult);

    
}
