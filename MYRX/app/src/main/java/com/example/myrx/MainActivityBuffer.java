package com.example.myrx;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrx.data.DataSource;
import com.example.myrx.data.Task;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;

public class MainActivityBuffer extends AppCompatActivity {

    private String TAG = "RXJAVA";
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        Observable<List<Task>> taskObservable = Observable.fromIterable(DataSource.createTasksList())
//                .buffer(2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//        taskObservable.subscribe(new Observer<List<Task>>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull List<Task> tasks) {
//                Log.d(TAG,"On Next Called  ========== ");
//                for (Task task:tasks) {
//                    Log.d(TAG,"On Next Called "+task.getDescription());
//                }
//
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });




        RxView.clicks(findViewById(R.id.button))
                .map(new Function<Unit, Integer>() {

                    @Override
                    public Integer apply(@NotNull Unit unit) throws Exception {
                        return 1;
                    }
                })
                .buffer(4, TimeUnit.SECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NotNull io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull List<Integer> integers) {
                        Log.d(TAG,"On Next Called  ========== ");
                        Log.d(TAG,"On Next Called "+integers.size());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

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