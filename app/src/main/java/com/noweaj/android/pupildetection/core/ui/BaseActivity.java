package com.noweaj.android.pupildetection.core.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 100;

    protected abstract void initView();
    protected abstract void onCameraPermissionGranted();
    protected abstract void enableView();
    protected abstract void disableView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableView();
    }

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
        disableView();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        boolean havePermission = true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if(havePermission){
            Log.d(TAG, "havePermission");
            onCameraPermissionGranted();
            onExternalStoragePermissionGranted();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE
                && grantResults.length > 0){
            boolean permFlag = true;
            for(int i=0; i<grantResults.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    showDialogForPermission("OpenCV requires following permission:\n"+permissions[i]);
                    permFlag = false;
                    break;
                }
            }
            if(permFlag) {
                onCameraPermissionGranted();
                onExternalStoragePermissionGranted();
            }
        } else {
            showDialogForPermission("Invalid request");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void showDialogForPermission(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
                    }
                }).setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private void onExternalStoragePermissionGranted(){
        String[] filesInAssets = new String[0];
        try{
            filesInAssets = getAssets().list("");
        } catch (IOException e){
            e.printStackTrace();
        }

        if(filesInAssets.length < 1){
            Log.d(TAG, "Cannot load cascade files from assets directory.");
            showDialogForPermission("Cannot load cascade files from assets directory.");
            return;
        }

        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String[] targetDirs = new String[filesInAssets.length];
        for(int i=0; i<targetDirs.length; i++){
            String filename = filesInAssets[i];
            Log.d(TAG, "Current file from assets directory: "+filename);
            targetDirs[i] = baseDir+File.separator+filename;
            Log.d(TAG, "Copy destination: "+targetDirs[i]);
            File file = new File(targetDirs[i]);
            if(!file.exists()) { // check if file exists
                Log.d(TAG, "File "+targetDirs[i]+" does not exist. Copying ...");
                InputStream is;
                OutputStream os;
                try {
                    is = getAssets().open(filename);
                    os = new FileOutputStream(targetDirs[i]);

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    is.close();
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "File "+targetDirs[i]+" does exist. Skip copying.");
            }
        }
    }
}
