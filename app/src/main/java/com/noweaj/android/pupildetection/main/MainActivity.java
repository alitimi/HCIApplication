package com.noweaj.android.pupildetection.main;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.noweaj.android.pupildetection.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

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

//    public native void ConvertRGBtoGray(long a, long b);

    private CameraBridgeViewBase camera;

    private void initView(){
        camera = findViewById(R.id.jcv_camera);
        camera.setVisibility(SurfaceView.VISIBLE);
        camera.setCvCameraViewListener(this);
        camera.setCameraIndex(1); //front 1, back 0
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
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
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
            if(checkSelfPermission(CAMERA_SERVICE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{CAMERA_SERVICE}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if(havePermission){
            onCameraPermissionGranted();
        }
    }

    @Override
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
                requestPermissions(new String[]{CAMERA_SERVICE}, CAMERA_PERMISSION_REQUEST_CODE);
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
    protected void onStop() {
        super.onStop();
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
        return null;
    }
}
