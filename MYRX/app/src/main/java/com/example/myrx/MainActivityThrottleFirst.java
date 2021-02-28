package com.example.myrx;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.Unit;

public class MainActivityThrottleFirst extends AppCompatActivity {

    private String TAG = "RXJAVA";
    private long timeSinceLastRequest;
    //ui
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.button);

        timeSinceLastRequest = System.currentTimeMillis();

        // Set a click listener to the button with RxBinding Library
        RxView.clicks(button)
                .throttleFirst(500, TimeUnit.MILLISECONDS) // Throttle the clicks so 500 ms must pass before registering a new click
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {

                    @Override
                    public void onSubscribe(@NotNull io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(Unit unit) {
                        Log.d(TAG, "onNext: time since last clicked: " + (System.currentTimeMillis() - timeSinceLastRequest));
                        someMethod(); // Execute some method when a click is registered
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }


    private void someMethod(){
        timeSinceLastRequest = System.currentTimeMillis();
        // do something
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        disposables.clear(); // clear disposables
    }
}