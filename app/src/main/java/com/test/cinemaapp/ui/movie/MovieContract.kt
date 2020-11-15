package com.test.cinemaapp.ui.movie

import android.content.Context
import com.test.cinemaapp.data.model.ResultsMovies
import com.test.cinemaapp.data.model.ResultsMoviesResponse
import com.test.cinemaapp.ui.base.BaseContract

class MovieContract {

    interface View: BaseContract.View{

        fun onDomainSuccess(resultsMovies: ResultsMoviesResponse)
        fun onMovieListSuccess(resultsMovies: ArrayList<ResultsMovies?>)
        fun onDomainError(msg: String)
        fun onMovieListError(msg: String)
        fun noResult()

    }

    interface Presenter: BaseContract.Presenter<View>{
        fun getMovie(context: Context, genreId : String, page : Int)
        fun getMovieList(context: Context, genreId : String, page : Int)
    }
}