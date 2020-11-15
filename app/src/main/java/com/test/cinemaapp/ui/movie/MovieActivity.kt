package com.test.cinemaapp.ui.movie

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.ResultsMovies
import com.test.cinemaapp.data.model.ResultsMoviesResponse
import com.test.cinemaapp.di.component.DaggerActivityComponent
import com.test.cinemaapp.di.module.ActivityModule
import com.test.cinemaapp.util.OnLoadMoreListener
import com.test.cinemaapp.util.RecyclerViewLoadMoreScroll
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject

class MovieActivity : AppCompatActivity() , MovieContract.View{
    @Inject
    lateinit var presenter: MovieContract.Presenter
    var genreId = ""
    private lateinit var adapterResultsMovies: ResultsMoviesAdapter
    private lateinit var loadMoreResultsMovies: ArrayList<ResultsMovies?>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    var page: Int = 1
    var totalPages: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        genreId = intent.getStringExtra("genreId")!!
        genreMovie.text = intent.getStringExtra("genreName")!!
        injectDependency()
        presenter.attach(this)
        progressbar.visibility = View.VISIBLE
        presenter.getMovie(this,genreId!!,page)

    }

    override fun onDomainSuccess(resultsMovie: ResultsMoviesResponse) {
        progressbar.visibility = View.GONE
        totalPages = resultsMovie.totalPages
        adapterResultsMovies =
            ResultsMoviesAdapter(
                resultsMovie.results
            )
        adapterResultsMovies.notifyDataSetChanged()
        mLayoutManager = GridLayoutManager(this,2)
        movieRv.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            removeAllViews()
            adapter = adapterResultsMovies
        }
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(this, R.color.colorPrimary))
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
        itemsswipetorefresh.setOnRefreshListener {
            page = 1
            resultsMovie.results.clear()
            adapterResultsMovies.addData(resultsMovie.results)
            adapterResultsMovies.notifyDataSetChanged()
            Handler().postDelayed(
                {
                    progressbar.visibility = View.VISIBLE
                    presenter.getMovie(this,genreId,page)
                    itemsswipetorefresh.isRefreshing = false
                },1000
            )
        }

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        movieRv.addOnScrollListener(scrollListener)
    }

    override fun onMovieListSuccess(resultsMovies: ArrayList<ResultsMovies?>) {
        if (resultsMovies.size > 0) {
            loadMoreResultsMovies.addAll(resultsMovies)
            adapterResultsMovies.removeLoadingView()
            adapterResultsMovies.addData(loadMoreResultsMovies)
            scrollListener.setLoaded()
            movieRv.post {
                adapterResultsMovies.notifyDataSetChanged()
            }
        } else {
            adapterResultsMovies.isLastItem()
        }
    }

    private fun loadMoreData() {
        loadMoreResultsMovies = ArrayList()
        if (page < totalPages){
            adapterResultsMovies.addLoadingView()
            page = page.inc()
            presenter.getMovieList(this,genreId,page)
        }else{
            adapterResultsMovies.isLastItem()
        }

    }

    override fun onDomainError(msg: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
        Log.d("Error : ",msg)
    }

    override fun onMovieListError(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
        adapterResultsMovies.isLastItem()
        Log.d("Error : ",msg)
    }

    override fun noResult() {
        progressbar.visibility = View.GONE
        itemsswipetorefresh.visibility = View.GONE
        noResult.visibility = View.VISIBLE
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.injectMovie(this)
    }
}