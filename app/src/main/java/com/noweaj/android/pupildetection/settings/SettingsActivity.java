package com.noweaj.android.pupildetection.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.noweaj.android.pupildetection.R;
import com.noweaj.android.pupildetection.rxjava.EditTextHandler;

import org.opencv.android.CameraBridgeViewBase;

import io.reactivex.disposables.Disposable;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private CameraBridgeViewBase jcv_settings_camera;
    private ImageView iv_settings_preview;

    private SeekBar sb_settings_scaling,
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

    private Disposable disposable_et_scaling,
            disposable_et_brightness,
            disposable_et_contrast,
            disposable_et_edge,
            disposable_et_gamma,
            disposable_sb_scaling,
            disposable_sb_brightness,
            disposable_sb_contrast,
            disposable_sb_edge,
            disposable_sb_gamma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView(){
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

        observeEditText();
        observeSeekBar();
    }

    private void observeEditText(){
        disposable_et_scaling = new EditTextHandler().setEditTextWatcher(et_settings_scaling, this);
        disposable_et_brightness = new EditTextHandler().setEditTextWatcher(et_settings_brightness, this);
        disposable_et_contrast = new EditTextHandler().setEditTextWatcher(et_settings_contrast, this);
        disposable_et_edge = new EditTextHandler().setEditTextWatcher(et_settings_edge, this);
        disposable_et_gamma = new EditTextHandler().setEditTextWatcher(et_settings_gamma, this);
    }

    private void observeSeekBar(){
        disposable_sb_scaling = new EditTextHandler().setSeekBarWatcher(sb_settings_scaling, this, et_settings_scaling);
        disposable_sb_brightness = new EditTextHandler().setSeekBarWatcher(sb_settings_brightness, this, et_settings_brightness);
        disposable_sb_contrast = new EditTextHandler().setSeekBarWatcher(sb_settings_contrast, this, et_settings_contrast);
        disposable_sb_edge = new EditTextHandler().setSeekBarWatcher(sb_settings_edge, this, et_settings_edge);
        disposable_sb_gamma = new EditTextHandler().setSeekBarWatcher(sb_settings_gamma, this, et_settings_gamma);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_settings_capture:
                break;
            case R.id.b_settings_save:
                break;
            case R.id.b_settings_reset:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    private void dispose(){
        Log.d(TAG, "dispose disposables");
        disposable_et_scaling.dispose();
        disposable_et_brightness.dispose();
        disposable_et_contrast.dispose();
        disposable_et_edge.dispose();
        disposable_et_gamma.dispose();

        disposable_sb_scaling.dispose();
        disposable_sb_brightness.dispose();
        disposable_sb_contrast.dispose();
        disposable_sb_edge.dispose();
        disposable_sb_gamma.dispose();
    }
}
