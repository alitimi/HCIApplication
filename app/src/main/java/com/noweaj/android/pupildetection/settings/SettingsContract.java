package com.noweaj.android.pupildetection.settings;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.noweaj.android.pupildetection.core.ui.BasePresenter;
import com.noweaj.android.pupildetection.core.ui.BaseView;

import org.opencv.android.CameraBridgeViewBase;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {
        void finishActivity();
    }

    interface Presenter extends BasePresenter {
        void observeEditText(EditText editText, Activity activity, SeekBar seekbar, int max);
        void observeSeekBar(SeekBar seekBar, Activity activity, EditText editText);
        void observeButton(Button button, int flag);
        CameraBridgeViewBase.CvCameraViewListener2 getCvCameraViewListener();
    }
}
