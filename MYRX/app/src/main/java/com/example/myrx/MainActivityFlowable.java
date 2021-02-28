package com.example.myrx;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrx.data.Task;

import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityFlowable extends AppCompatActivity {

    private String TAG = "RXJAVA";
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Flowable.range(0, 1000000)
                .onBackpressureBuffer()
                .observeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }
                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: ", t);
                    }
                    @Override
                    public void onComplete() {

                    }
                });


        //Observable<Integer> observable = Observable
         //       .just(1, 2, 3, 4, 5);

        //Flowable<Integer> flowable = observable.toFlowable(BackpressureStrategy.BUFFER);



//        Observable<Integer> observable = Observable
//                .just(1, 2, 3, 4, 5);
//
//        Flowable<Integer> flowable = observable.toFlowable(BackpressureStrategy.BUFFER);
//        Observable<Integer> backToObservable = flowable.toObservable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        // disposable.dispose();
    }
}