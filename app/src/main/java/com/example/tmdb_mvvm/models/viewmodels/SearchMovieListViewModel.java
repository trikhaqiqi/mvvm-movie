package com.example.tmdb_mvvm.models.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tmdb_mvvm.models.MovieModel;
import com.example.tmdb_mvvm.repositories.SearchMovieRepository;

import java.util.List;

public class SearchMovieListViewModel extends ViewModel {
    private SearchMovieRepository searchMovieRepository;

    public SearchMovieListViewModel() {
        searchMovieRepository = SearchMovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getSearchMovie() {
        return searchMovieRepository.getSearchMovie();
    }

    public void getSearchMovie(String query,int page) {
        searchMovieRepository.getSearchMovie(query,page);
    }

    // next page
    public void searchMovieNextPage() {
        searchMovieRepository.searchMovieNextPage();

    }
}
