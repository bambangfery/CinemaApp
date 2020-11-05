package com.test.cinemaapp.di.module

import android.app.Activity
import com.test.cinemaapp.ui.detailsmovie.DetailsMovieContract
import com.test.cinemaapp.ui.detailsmovie.DetailsMoviePresenter
import com.test.cinemaapp.ui.genre.GenreContract
import com.test.cinemaapp.ui.genre.GenrePresenter
import com.test.cinemaapp.ui.movie.MovieContract
import com.test.cinemaapp.ui.movie.MoviePresenter
import com.test.cinemaapp.ui.review.ReviewContract
import com.test.cinemaapp.ui.review.ReviewPresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: Activity) {
    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun provideMainPresenter(): GenreContract.Presenter {
        return GenrePresenter()
    }

    @Provides
    fun provideMoviePresenter(): MovieContract.Presenter {
        return MoviePresenter()
    }

    @Provides
    fun provideDetailsMoviePresenter(): DetailsMovieContract.Presenter {
        return DetailsMoviePresenter()
    }

    @Provides
    fun provideReviewMoviePresenter(): ReviewContract.Presenter {
        return ReviewPresenter()
    }

}