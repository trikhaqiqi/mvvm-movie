package com.example.tmdb_mvvm.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmdb_mvvm.models.MovieModel;
import com.example.tmdb_mvvm.response.PopularMovieResponses;
import com.example.tmdb_mvvm.utils.AppExecutor;
import com.example.tmdb_mvvm.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class PopularMovieApiClient {
    private static PopularMovieApiClient instance;
    public static PopularMovieApiClient getInstance() {
        if (instance == null) {
            instance = new PopularMovieApiClient();
        }
        return instance;
    }

    // live data
    private final MutableLiveData<List<MovieModel>> popularMovieLiveData;

    // global variable for runnable
    private PopularRunnable popularRunnable;

    private PopularMovieApiClient() {
        popularMovieLiveData = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getPopularMovie() {
        return popularMovieLiveData;
    }

    public void getPopularMovie(int page) {
        if (popularRunnable != null) {
            popularRunnable = null;
        }

        popularRunnable = new PopularRunnable(page);

        final Future handler = AppExecutor.getInstance().getNetworkIO().submit(popularRunnable);

        AppExecutor.getInstance().getNetworkIO().schedule(() -> {
            // canceling retrofit call
            handler.cancel(true);
        },3000, TimeUnit.MILLISECONDS);
    }

    // retrieve data from rest api using runnable
    private class PopularRunnable implements Runnable {

        private final int page;
        boolean cancleRequest;

        public PopularRunnable(int page) {
            this.page = page;
            cancleRequest = false;
        }

        @Override
        public void run() {
            // getting request
            try {
                Response response = getPopularMovie(page).execute();

                if (cancleRequest) {
                    return;
                }

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        List<MovieModel> popularMovieList = new ArrayList<>(((PopularMovieResponses) response.body()).getMovies());

                        if (page == 1) {
                            // send data to live data
                            // post value -> using banckground thread
                            // set value
                            popularMovieLiveData.postValue(popularMovieList);
                        } else {
                            List<MovieModel> currentMovie = popularMovieLiveData.getValue();
                            assert currentMovie != null;
                            currentMovie.addAll(popularMovieList);
                            popularMovieLiveData.postValue(currentMovie);
                        }
                    } else {
                        assert response.errorBody() != null;
                        System.out.println(response.errorBody().string());
                        popularMovieLiveData.postValue(null);
                    }
                } else {
                    System.out.println("request isnt successful");
                }
            } catch (IOException e) {
                System.out.println(e);
                popularMovieLiveData.postValue(null);
            }
        }

        // method getPopularMovie
        private Call<PopularMovieResponses> getPopularMovie(int page) {
            return ApiService.getMovieApi().getPopularMovie(Credentials.KEY,page);
        }
    }
}

