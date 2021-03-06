package com.test.cinemaapp.ui.review

import android.content.Context
import com.test.cinemaapp.data.model.ReviewMovie
import com.test.cinemaapp.data.model.ReviewMovieResponse
import com.test.cinemaapp.ui.base.BaseContract

class ReviewContract {
    interface View: BaseContract.View{

        fun onDomainSuccess(reviewMovieResponse: ReviewMovieResponse)
        fun onDomainError(msg: String)
        fun onReviewListSuccess(reviewMovie: ArrayList<ReviewMovie?>)
        fun onReviewListError(msg: String)

    }

    interface Presenter: BaseContract.Presenter<View>{
        fun getReview(context: Context, movieId: String, page: Int)
        fun getReviewList(context: Context, movieId: String, page: Int)
    }
}