package com.example.myrx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityDebounce extends AppCompatActivity {

    private String TAG = "RXJAVA";
    private long timeSinceLastRequest;
    //ui
    private androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_debounce);
        searchView = findViewById(R.id.search_view);
        timeSinceLastRequest = System.currentTimeMillis();

        Observable<String> observable  = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(!emitter.isDisposed())
                            emitter.onNext(newText);
                        return false;
                    }
                });
            }
        })
          .debounce(500, TimeUnit.MILLISECONDS)
           .subscribeOn(Schedulers.io());


        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {

                Log.d(TAG, "onNext: time  since last request: "
                        + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();
                // method for sending a request to the server
                sendRequestToServer(s);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    private void sendRequestToServer(String query){
        // do nothing
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        disposables.clear(); // clear disposables
    }
}