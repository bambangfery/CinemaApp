package com.test.cinemaapp.di.component

import com.test.cinemaapp.ui.genre.GenreActivity
import com.test.cinemaapp.di.module.ActivityModule
import com.test.cinemaapp.ui.detailsmovie.DetailsMovieActivity
import com.test.cinemaapp.ui.movie.MovieActivity
import com.test.cinemaapp.ui.review.ReviewActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun injectMain(genreActivity: GenreActivity)
    fun injectMovie(movieActivity: MovieActivity)
    fun injectDetailsMovie(detailsMovieActivity: DetailsMovieActivity)
    fun injectReview(reviewActivity: ReviewActivity)

}