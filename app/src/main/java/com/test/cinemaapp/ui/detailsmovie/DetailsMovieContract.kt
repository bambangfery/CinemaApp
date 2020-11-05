package com.test.cinemaapp.ui.detailsmovie

import com.test.cinemaapp.data.model.DetailsMovieResponse
import com.test.cinemaapp.data.model.ReviewMovieResponse
import com.test.cinemaapp.data.model.TrailerMovieResponse
import com.test.cinemaapp.ui.base.BaseContract

class DetailsMovieContract {
    interface View: BaseContract.View{

        fun onDomainSuccess(detailsMovieResponse: DetailsMovieResponse)
        fun onDomainError(msg: String)
        fun onTrailerSuccess(trailerMovieResponse: TrailerMovieResponse)
        fun onTrailerError(msg: String)
        fun onReviewSuccess(reviewMovieResponse: ReviewMovieResponse)
        fun onReviewError(msg: String)

    }

    interface Presenter: BaseContract.Presenter<View>{
        fun getDetailsMovie(movieId : String)
        fun getTrailersMovie(movieId : String)
        fun getReviewMovie(movieId : String)
    }
}