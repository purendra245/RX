package com.example.myrx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrx.data.Comment;
import com.example.myrx.data.Post;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivitySwitchMap extends AppCompatActivity implements RecyclerAdapterNew.OnPostClickListener  {
    private static final String TAG = "RXJAVA";

        //ui
        private RecyclerView recyclerView;
        private ProgressBar progressBar;

        // vars
        private CompositeDisposable disposables = new CompositeDisposable();
        private RecyclerAdapterNew adapter;
        private PublishSubject<Post> publishSubject = PublishSubject.create(); // for selecting a post
        private static final int PERIOD = 100;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_flat_map);
            recyclerView = findViewById(R.id.recycler_view);
            progressBar = findViewById(R.id.progress_bar);

            initRecyclerView();
            retrievePosts();
        }

        private void initSwitchMapDemo() {
            publishSubject

                    // apply switchmap operator so only one Observable can be used at a time.
                    // it clears the previous one
                    .switchMap(new Function<Post, ObservableSource<Post>>() {
                        @Override
                        public ObservableSource<Post> apply(final Post post) throws Exception {
                            return Observable

                                    // simulate slow network speed with interval + takeWhile + filter operators
                                    .interval(PERIOD, TimeUnit.MILLISECONDS)
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .takeWhile(new Predicate<Long>() { // stop the process if more than 5 seconds passes
                                        @Override
                                        public boolean test(Long aLong) throws Exception {
                                            Log.d(TAG, "test: " + Thread.currentThread().getName() + ", " + aLong);
                                            progressBar.setMax(3000 - PERIOD);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((aLong * PERIOD) + PERIOD)));
                                            return aLong <= (3000 / PERIOD);
                                        }
                                    })
                                    .filter(new Predicate<Long>() {
                                        @Override
                                        public boolean test(Long aLong) throws Exception {
                                            return aLong >= (3000 / PERIOD);
                                        }
                                    })

                                    // flatmap to convert Long from the interval operator into a Observable<Post>
                                    .subscribeOn(Schedulers.io())
                                    .flatMap(new Function<Long, ObservableSource<Post>>() {
                                        @Override
                                        public ObservableSource<Post> apply(Long aLong) throws Exception {
                                            return ServiceGenerator.getRequestApi()
                                                    .getPost(post.getId());
                                        }
                                    });
                        }
                    })
                    .subscribe(new Observer<Post>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onNext(Post post) {
                            Log.d(TAG, "onNext: done.");
                            navViewPostActivity(post);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        private void retrievePosts() {
            ServiceGenerator.getRequestApi()
                    .getPosts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Post>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onNext(List<Post> posts) {
                            adapter.setPosts(posts);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        protected void onResume() {
            super.onResume();
            progressBar.setProgress(0);
            initSwitchMapDemo();
        }

        private void initRecyclerView() {
            adapter = new RecyclerAdapterNew(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        private void navViewPostActivity(Post post) {
            Intent intent = new Intent(this, ViewPostActivity.class);
            intent.putExtra("post", post);
            startActivity(intent);
        }

        @Override
        protected void onPause() {
            Log.d(TAG, "onPause: called.");
            disposables.clear();
            super.onPause();
        }

        @Override
        public void onPostClick(final int position) {

            Log.d(TAG, "onPostClick: clicked.");

            // submit the selected post object to be queried
            publishSubject.onNext(adapter.getPosts().get(position));
        }
    }
