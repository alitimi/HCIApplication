package com.noweaj.android.pupildetection.main;

import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainPresenter implements MainContract.Presenter{

    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View mView;

    public MainPresenter(MainContract.View mView){
        this.mView = mView;
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
    public void observeButton(Button button, int flag) {
        Disposable disposable = RxView.clicks(button)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(data -> {
                    switch (flag){
                        case 0: // register
                            break;
                        case 1: // verify
                            break;
                        case 2: // delete
                            break;
                        case 3: // cancel
                            break;
                        case 4: // save
                            break;
                        case 5: // settings
                            mView.startSettingsActivity();
                            break;
                        default:
                            break;
                    }
                }, error -> {
                    Log.d(TAG, error.getLocalizedMessage());
                });
        mCompositeDisposable.add(disposable);
    }
}
