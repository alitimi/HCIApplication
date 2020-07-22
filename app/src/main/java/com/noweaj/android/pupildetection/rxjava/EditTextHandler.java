package com.noweaj.android.pupildetection.rxjava;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;

import com.jakewharton.rxbinding3.widget.RxSeekBar;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public class EditTextHandler {

    private static final String TAG = EditTextHandler.class.getSimpleName();

    public Disposable setEditTextWatcher(EditText editText, Activity activity){
        return RxTextView.textChanges(editText)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if(TextUtils.isEmpty(charSequence)){
                        Log.d(TAG, "field empty");
                        // set seekbar to 0
                    } else if(Integer.parseInt(charSequence.toString()) < 0){
                        Log.d(TAG, "value less than 0");
                        activity.runOnUiThread(() -> {
                            editText.setText("0");
                            // set seekbar to 0
                        });
                    } else if(Integer.parseInt(charSequence.toString()) > 100){
                        Log.d(TAG, "value more than 100");
                        activity.runOnUiThread(() -> {
                            editText.setText("100");
                            // set seekbar to 100
                        });
                    } else {
                        Log.d(TAG, "value "+charSequence);
                        // set seekbar to value
                    }
                }, error -> {
                    Log.e(TAG, "");
                });
    }

    public Disposable setSeekBarWatcher(SeekBar seekBar, Activity activity, EditText editText){
        return RxSeekBar.changes(seekBar)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    activity.runOnUiThread(() -> {
                        editText.setText(integer+"");
                    });
                });
    }
}
