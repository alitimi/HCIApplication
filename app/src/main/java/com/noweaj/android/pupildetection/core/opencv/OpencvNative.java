package com.noweaj.android.pupildetection.core.opencv;

public class OpencvNative {
    public OpencvNative(){}
    // Native
    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public native long LoadCascade(String cascadeFileName);
}
