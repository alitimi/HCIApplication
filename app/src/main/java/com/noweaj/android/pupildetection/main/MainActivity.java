package com.noweaj.android.pupildetection.main;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.noweaj.android.pupildetection.R;
import com.noweaj.android.pupildetection.settings.SettingsActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, MainContract.View, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        initView();
    }

    private CameraBridgeViewBase camera;
    private TextView tv_instruction;
    private Button b_register, b_verify, b_delete, b_cancel, b_save, b_settings;

    private void initView(){
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // RESULT_OK
            } else {
                // RESULT_CANCELED || FAILED
            }
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    camera.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "onResume: Internal OpenCV library not found");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResume: OpenCV library found inside package");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(camera != null)
            camera.disableView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean havePermission = true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if(havePermission){
            onCameraPermissionGranted();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            onCameraPermissionGranted();
        } else {
            showDialogForPermission("OpenCV requires camera permission!");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void onCameraPermissionGranted(){
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

    private void showDialogForPermission(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(camera != null)
            camera.disableView();
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
