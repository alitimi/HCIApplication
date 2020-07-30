package com.noweaj.android.pupildetection.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.noweaj.android.pupildetection.R;
import com.noweaj.android.pupildetection.core.ui.BaseActivity;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsContract.Presenter mPresenter;

    private CameraBridgeViewBase jcv_settings_camera;
    private ImageView iv_settings_preview;

    public SeekBar sb_settings_scaling,
            sb_settings_brightness,
            sb_settings_contrast,
            sb_settings_edge,
            sb_settings_gamma;
    private EditText et_settings_scaling,
            et_settings_brightness,
            et_settings_contrast,
            et_settings_edge,
            et_settings_gamma;
    private Button b_settings_capture,
            b_settings_save,
            b_settings_reset;

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void initView(){
        setContentView(R.layout.activity_setting);

        jcv_settings_camera = findViewById(R.id.jcv_settings_camera);
        jcv_settings_camera.setVisibility(SurfaceView.VISIBLE);
        jcv_settings_camera.setCvCameraViewListener(this);
        jcv_settings_camera.setCameraIndex(1);
        jcv_settings_camera.enableFpsMeter();

        b_settings_capture = findViewById(R.id.b_settings_capture);
        b_settings_save = findViewById(R.id.b_settings_save);
        b_settings_reset = findViewById(R.id.b_settings_reset);

        et_settings_scaling = findViewById(R.id.et_settings_scaling);
        et_settings_brightness = findViewById(R.id.et_settings_brightness);
        et_settings_contrast = findViewById(R.id.et_settings_contrast);
        et_settings_edge = findViewById(R.id.et_settings_edge);
        et_settings_gamma = findViewById(R.id.et_settings_gamma);

        sb_settings_scaling = findViewById(R.id.sb_settings_scaling);
        sb_settings_brightness = findViewById(R.id.sb_settings_brightness);
        sb_settings_contrast = findViewById(R.id.sb_settings_contrast);
        sb_settings_edge = findViewById(R.id.sb_settings_edge);
        sb_settings_gamma = findViewById(R.id.sb_settings_gamma);

        mPresenter = new SettingsPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        observe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    protected void enableView() {
        jcv_settings_camera.enableView();
    }

    @Override
    protected void disableView() {
        if(jcv_settings_camera != null)
            jcv_settings_camera.disableView();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCameraPermissionGranted(){
        Log.d(TAG, "onCameraPermissionGranted");
        List<? extends CameraBridgeViewBase> cameraViews = Collections.singletonList(jcv_settings_camera);
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

    private void observe(){
        mPresenter.observeEditText(et_settings_scaling, this, sb_settings_scaling);
        mPresenter.observeEditText(et_settings_brightness, this, sb_settings_brightness);
        mPresenter.observeEditText(et_settings_contrast, this, sb_settings_contrast);
        mPresenter.observeEditText(et_settings_edge, this, sb_settings_edge);
        mPresenter.observeEditText(et_settings_gamma, this, sb_settings_gamma);

        mPresenter.observeSeekBar(sb_settings_scaling, this, et_settings_scaling);
        mPresenter.observeSeekBar(sb_settings_brightness, this, et_settings_brightness);
        mPresenter.observeSeekBar(sb_settings_contrast, this, et_settings_contrast);
        mPresenter.observeSeekBar(sb_settings_edge, this, et_settings_edge);
        mPresenter.observeSeekBar(sb_settings_gamma, this, et_settings_gamma);

        mPresenter.observeButton(b_settings_capture);
        mPresenter.observeButton(b_settings_save);
        mPresenter.observeButton(b_settings_reset);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat matInput = inputFrame.rgba();
        return matInput;
    }

}
