package com.noweaj.android.pupildetection.settings;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.noweaj.android.pupildetection.R;
import com.noweaj.android.pupildetection.databinding.ActivitySettingBinding;

import org.opencv.android.CameraBridgeViewBase;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

//    private CameraBridgeViewBase jcv_settings_camera;
//    private ImageView iv_settings_preview;

//    private SeekBar sb_settings_scaling, sb_settings_brightness, sb_settings_contrast, sb_settings_edge, sb_settings_gamma;
//    private EditText et_settings_scaling, et_settings_brightness, et_settings_contrast, et_settings_edge, et_settings_gamma;

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        initView();
    }

    private void initView(){
        binding.bSettingsCapture.setOnClickListener(this);
        binding.bSettingsSave.setOnClickListener(this);
        binding.bSettingsReset.setOnClickListener(this);

        observeEdittext();
    }

    private void observeEdittext(){
//        Observable obS = RxTextView.textChanges(binding.etSettingsScaling);
//        obS.subscribe(charsequence->{
//            Log.d(TAG, "scaling result: "+charsequence);
//        });

        RxTextView.textChanges(binding.etSettingsScaling)
                .filter(charSequence -> Integer.parseInt(charSequence.toString()) > -1 && Integer.parseInt(charSequence.toString()) < 100)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        Observable obB = RxTextView.textChanges(binding.etSettingsBrightness);
        obB.subscribe(charsequence->{
            Log.d(TAG, "brightness result: "+charsequence);
        });

        Observable obC = RxTextView.textChanges(binding.etSettingsContrast);
        obC.subscribe(charsequence->{
            Log.d(TAG, "contrast result: "+charsequence);
        });

        Observable obE = RxTextView.textChanges(binding.etSettingsEdge);
        obE.subscribe(charsequence->{
            Log.d(TAG, "edge result: "+charsequence);
        });

        Observable obG = RxTextView.textChanges(binding.etSettingsGamma);
        obG.subscribe(charsequence->{
            Log.d(TAG, "gamma result: "+charsequence);
        });
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
}
