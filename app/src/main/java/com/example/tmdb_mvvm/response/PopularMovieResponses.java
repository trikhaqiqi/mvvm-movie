package com.example.tmdb_mvvm.response;

import com.example.tmdb_mvvm.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularMovieResponses {
    @SerializedName("results")
    @Expose
    private List<MovieModel> movies;

    public List<MovieModel> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "MovieResponses{" +
                "movies=" + movies +
                '}';
    }
}