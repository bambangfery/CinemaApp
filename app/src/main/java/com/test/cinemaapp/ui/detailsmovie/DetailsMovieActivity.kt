package com.test.cinemaapp.ui.detailsmovie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.DetailsMovieResponse
import com.test.cinemaapp.data.model.ReviewMovieResponse
import com.test.cinemaapp.data.model.TrailerMovieResponse
import com.test.cinemaapp.di.component.DaggerActivityComponent
import com.test.cinemaapp.di.module.ActivityModule
import com.test.cinemaapp.ui.review.ReviewActivity
import com.test.cinemaapp.util.Constants
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_details_movie.*
import javax.inject.Inject

class DetailsMovieActivity : AppCompatActivity() , DetailsMovieContract.View{
    @Inject
    lateinit var presenter: DetailsMovieContract.Presenter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    val adapterGenreList = GroupAdapter<ViewHolder>()
    var movieId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)
        movieId = intent.getStringExtra("movieId")!!
        injectDependency()
        presenter.attach(this)
        presenter.getDetailsMovie(movieId)

    }

    override fun onDomainSuccess(details: DetailsMovieResponse) {
        Picasso.get()
            .load(Constants.IMAGE_ORI+details.backdropPath)
            .error(resources.getDrawable(R.mipmap.no_image))
            .into(banner)

        titleMovie.text = details.title
        releaseDate.text = details.releaseDate
        ratings.text = details.voteCount.toString()+" Ratings"

        adapterGenreList.apply {
            clear()
            notifyDataSetChanged()
        }
        details.genres.forEach {
            adapterGenreList.add(GenreListAdapter(details.genres))
        }
        mLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        genreListRv.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            removeAllViews()
            adapter = adapterGenreList
        }

        overview.text = details.overview
        presenter.getReviewMovie(movieId)
        presenter.getTrailersMovie(movieId)
    }

    override fun onDomainError(msg: String) {
        Log.d("Error : ",msg)
    }

    override fun onTrailerSuccess(trailerMovieResponse: TrailerMovieResponse) {
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = trailerMovieResponse.results[0].key
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.pause()
            }
        })
    }

    override fun onTrailerError(msg: String) {
        youtubePlayerView.visibility = View.INVISIBLE
        Log.d("Error : ",msg)
    }

    override fun onReviewSuccess(reviewMovieResponse: ReviewMovieResponse) {
        review.text = reviewMovieResponse.results[0]?.content
        seeAllReview.setOnClickListener {
            val reviewIntent = Intent(this, ReviewActivity::class.java)
                .apply {
                    putExtra("movieId",movieId)
                }
            startActivity(reviewIntent)
        }
    }

    override fun onReviewError(msg: String) {
        review.text = "No Review"
        seeAllReview.visibility = View.GONE
        Log.d("Error : ",msg)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.injectDetailsMovie(this)
    }
}