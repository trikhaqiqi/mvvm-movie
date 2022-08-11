package com.example.tmdb_mvvm.utils;

import com.example.tmdb_mvvm.response.PopularMovieResponses;
import com.example.tmdb_mvvm.response.SearchMovieResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    // popular movie
    @GET("movie/popular")
    Call<PopularMovieResponses> getPopularMovie(@Query("api_key") String key,
                                                @Query("page") int page);

    // search movie
    @GET("search/movie/")
    Call<SearchMovieResponses> searchMovie(@Query("api_key") String key,
                                           @Query("query") String query,
                                           @Query("page") int page);
}

