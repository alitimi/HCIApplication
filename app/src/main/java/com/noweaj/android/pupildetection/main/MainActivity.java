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
import com.noweaj.android.pupildetection.core.ui.BaseActivity;
import com.noweaj.android.pupildetection.settings.SettingsActivity;
import com.noweaj.android.pupildetection.settings.SettingsContract;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 200;

    private MainContract.Presenter mPresenter;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    private CameraBridgeViewBase camera;
    private TextView tv_instruction;
    private Button b_register, b_verify, b_delete, b_cancel, b_save, b_settings;

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void initView(){
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        camera = findViewById(R.id.jcv_camera);
        camera.setVisibility(SurfaceView.VISIBLE);
        camera.setCvCameraViewListener(mPresenter.getCvCameraViewListener());
        camera.setCameraIndex(1); //front 1, back 0
        camera.enableFpsMeter();

        tv_instruction = findViewById(R.id.tv_instruction);

        b_register = findViewById(R.id.b_register);
        b_verify = findViewById(R.id.b_verify);
        b_delete = findViewById(R.id.b_delete);
        b_cancel = findViewById(R.id.b_cancel);
        b_save = findViewById(R.id.b_save);
        b_settings = findViewById(R.id.b_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        setClicks();
    }

    private void setClicks(){
        mPresenter.observeButton(b_register, 0);
        mPresenter.observeButton(b_verify, 1);
        mPresenter.observeButton(b_delete, 2);
        mPresenter.observeButton(b_cancel, 3);
        mPresenter.observeButton(b_save, 4);
        mPresenter.observeButton(b_settings, 5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
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

    @Override
    protected void onExternalStoragePermissionGranted() {

    }

    @Override
    public void startSettingsActivity() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_REQUEST_CODE);
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
}
