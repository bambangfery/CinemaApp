package com.test.cinemaapp.ui.review

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.ReviewMovie
import com.test.cinemaapp.data.model.ReviewMovieResponse
import com.test.cinemaapp.di.component.DaggerActivityComponent
import com.test.cinemaapp.di.module.ActivityModule
import com.test.cinemaapp.util.OnLoadMoreListener
import com.test.cinemaapp.util.RecyclerViewLoadMoreScroll
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_see_all_review.*
import kotlinx.android.synthetic.main.activity_see_all_review.itemsswipetorefresh
import kotlinx.android.synthetic.main.activity_see_all_review.progressbar
import javax.inject.Inject

class ReviewActivity : AppCompatActivity() , ReviewContract.View{
    @Inject
    lateinit var presenter: ReviewContract.Presenter
    private lateinit var adapterReviewMovies: ReviewListAdapter
    private lateinit var loadMoreReviewMovies: ArrayList<ReviewMovie?>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    var movieId = ""
    var page: Int = 1
    var totalPages: Int = 1
    var isFirst: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_review)
        movieId = intent.getStringExtra("movieId")!!
        injectDependency()
        presenter.attach(this)
        progressbar.visibility = View.VISIBLE
        presenter.getReview(movieId,page)
    }

    override fun onDomainSuccess(reviewMovieResponse: ReviewMovieResponse) {
        progressbar.visibility = View.GONE
        totalPages = reviewMovieResponse.totalPages
        adapterReviewMovies =
            ReviewListAdapter(
                reviewMovieResponse.results
            )
        adapterReviewMovies.notifyDataSetChanged()
        mLayoutManager = LinearLayoutManager(this)
        reviewRv.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            removeAllViews()
            adapter = adapterReviewMovies
        }

        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(this, R.color.colorPrimary))
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
        itemsswipetorefresh.setOnRefreshListener {
            page = 1
            isFirst = 1
            reviewMovieResponse.results.clear()
            adapterReviewMovies.addData(reviewMovieResponse.results)
            adapterReviewMovies.notifyDataSetChanged()
            Handler().postDelayed(
                {
                    progressbar.visibility = View.VISIBLE
                    presenter.getReview(movieId,page)
                    itemsswipetorefresh.isRefreshing = false
                },1000
            )
        }

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                if (isFirst==1){
                    isFirst=0
                }else{
                    loadMoreData()
                }
            }
        })
        reviewRv.addOnScrollListener(scrollListener)
    }

    private fun loadMoreData() {
        adapterReviewMovies.addLoadingView()
        loadMoreReviewMovies = ArrayList()
        if (page <= totalPages)
            page = page.inc()
        presenter.getReviewList(movieId,page)

    }

    override fun onDomainError(msg: String) {
        progressbar.visibility = View.GONE
    }

    override fun onReviewListSuccess(reviewMovie: ArrayList<ReviewMovie?>) {
        if (reviewMovie.size > 0) {
            loadMoreReviewMovies.addAll(reviewMovie)
            adapterReviewMovies.removeLoadingView()
            adapterReviewMovies.addData(loadMoreReviewMovies)
            scrollListener.setLoaded()
            movieRv.post {
                adapterReviewMovies.notifyDataSetChanged()
            }
        } else {
            adapterReviewMovies.removeLoadingView()
        }
    }

    override fun onReviewListError(msg: String) {
        Log.d("Error : ",msg)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.injectReview(this)
    }
}