package com.noweaj.android.pupildetection.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noweaj.android.pupildetection.R;
import com.noweaj.android.pupildetection.core.BaseActivity;
import com.noweaj.android.pupildetection.settings.SettingsActivity;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements CameraBridgeViewBase.CvCameraViewListener2, MainContract.View, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 200;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    private CameraBridgeViewBase camera;
    private TextView tv_instruction;
    private Button b_register, b_verify, b_delete, b_cancel, b_save, b_settings;

    @Override
    protected void initView(){
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.jcv_camera);
        camera.setVisibility(SurfaceView.VISIBLE);
        camera.setCvCameraViewListener(this);
        camera.setCameraIndex(1); //front 1, back 0
        camera.enableFpsMeter();

        b_register = findViewById(R.id.b_register);
        b_register.setOnClickListener(this);
        b_verify = findViewById(R.id.b_verify);
        b_verify.setOnClickListener(this);
        b_delete = findViewById(R.id.b_delete);
        b_delete.setOnClickListener(this);
        b_cancel = findViewById(R.id.b_cancel);
        b_cancel.setOnClickListener(this);
        b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(this);
        b_settings = findViewById(R.id.b_settings);
        b_settings.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_register:
                break;
            case R.id.b_verify:
                break;
            case R.id.b_delete:
                break;
            case R.id.b_cancel:
                break;
            case R.id.b_save:
                break;
            case R.id.b_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // RESULT_OK
            } else {
                // RESULT_CANCELED || FAILED
            }
        }
    }

    @Override
    protected void enableView() {
        camera.enableView();
    }

    @Override
    protected void disableView() {
        if(camera != null)
            camera.disableView();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCameraPermissionGranted(){
        Log.d(TAG, "onCameraPermissionGranted");
        List<? extends CameraBridgeViewBase> cameraViews = Collections.singletonList(camera);
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    // CameraBridgeViewBase.CvCameraViewListener2
    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat matInput = inputFrame.rgba();
//        Mat matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        ConvertRGBtoGray(matInput.getNativeObjAddr(), matInput.getNativeObjAddr());
        return matInput;
//        return  inputFrame.rgba();
    }

    // Native
    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public native long LoadCascade(String cascadeFileName);
}
