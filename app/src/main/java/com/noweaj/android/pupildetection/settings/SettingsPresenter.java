package com.noweaj.android.pupildetection.settings;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.jakewharton.rxbinding3.widget.RxSeekBar;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SettingsPresenter implements SettingsContract.Presenter {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    private SettingsContract.View mView;

    public SettingsPresenter(SettingsContract.View mView){
        this.mView = mView;
        mView.setPresenter(this);
    }

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void subscribe() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void observeEditText(EditText editText, Activity activity, SeekBar seekbar) {
        Disposable disposable = RxTextView.textChanges(editText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if(TextUtils.isEmpty(charSequence)){
                        Log.d(TAG, "field empty");
                    } else if(Integer.parseInt(charSequence.toString()) < 0){
                        Log.d(TAG, "value less than 0");
                        activity.runOnUiThread(() -> {
                            editText.setText("0");
                        });
                    } else if(Integer.parseInt(charSequence.toString()) > 100){
                        Log.d(TAG, "value more than limit(100)");
                        activity.runOnUiThread(() -> {
                            editText.setText("100");
                        });
                    } else {
                        Log.d(TAG, "value "+charSequence);
                        activity.runOnUiThread(() -> {
                            seekbar.setProgress(Integer.parseInt(charSequence.toString()));
                        });
                    }
                }, error -> {
                    Log.e(TAG, error.getLocalizedMessage());
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void observeSeekBar(SeekBar seekBar, Activity activity, EditText editText) {
        Disposable disposable = RxSeekBar.changes(seekBar)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    activity.runOnUiThread(() -> {
                        editText.setText(Integer.toString(integer));
                    });
                }, error -> {
                    Log.e(TAG, error.getLocalizedMessage());
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void observeButton(Button button) {

    }
}
