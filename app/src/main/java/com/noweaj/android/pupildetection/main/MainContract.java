package com.noweaj.android.pupildetection.main;

import android.widget.Button;

import com.noweaj.android.pupildetection.core.ui.BasePresenter;
import com.noweaj.android.pupildetection.core.ui.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void startSettingsActivity();
    }
    interface Presenter extends BasePresenter {
        void observeButton(Button button, int flag);
    }
}
