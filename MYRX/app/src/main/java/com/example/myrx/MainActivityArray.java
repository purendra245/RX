package com.example.myrx;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrx.data.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityArray extends AppCompatActivity {

    private String TAG = "RXJAVA";
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        Task[] list = new Task[5];
//        list[0] = (new Task("Take out the trash", true, 3));
//        list[1] = (new Task("Walk the dog", false, 2));
//        list[2] = (new Task("Make my bed", true, 1));
//        list[3] = (new Task("Unload the dishwasher", false, 0));
//        list[4] = (new Task("Make dinner", true, 5));
//
//        Observable<Task> taskObservable = Observable
//                .fromArray(list)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//        taskObservable.subscribe(new Observer<Task>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Task task) {
//                Log.d(TAG, "onNext: : " + task.getDescription());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


        // From Iteratable

//        List<Task> taskList = new ArrayList<>();
//        taskList.add(new Task("Take out the trash", true, 3));
//        taskList.add(new Task("Walk the dog", false, 2));
//        taskList.add(new Task("Make my bed", true, 1));
//        taskList.add(new Task("Unload the dishwasher", false, 0));
//        taskList.add(new Task("Make dinner", true, 5));
//
//        Observable<Task> taskObservable = Observable
//                .fromIterable(taskList)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//        taskObservable.subscribe(new Observer<Task>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Task task) {
//                Log.d(TAG, "onNext: : " + task.getDescription());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        Observable<Task> callable = Observable
                .fromCallable(new Callable<Task>() {
                    @Override
                    public Task call() throws Exception {
//                        return MyDatabase.getTask();
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        // method will be executed since now something has subscribed
        callable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: : " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        // disposable.dispose();
    }
}