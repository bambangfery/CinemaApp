package com.test.cinemaapp.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.ResultsMovies
import com.test.cinemaapp.ui.detailsmovie.DetailsMovieActivity
import com.test.cinemaapp.util.Constants
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.progress_loading.view.*


class ResultsMoviesAdapter(private var resultsMovies: ArrayList<ResultsMovies?>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var mcontext: Context
    var isLast: Boolean = false

    class ResultsMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<ResultsMovies?>) {
        this.resultsMovies.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            resultsMovies.add(null)
            notifyItemInserted(resultsMovies.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (resultsMovies.size != 0) {
            resultsMovies.removeAt(resultsMovies.size - 1)
            notifyItemRemoved(resultsMovies.size)
        }
    }

    fun isLastItem() {
        isLast = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Constants.VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_movie
                    , parent, false)
            ResultsMoviesViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(R.layout.progress_loading
                    , parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return resultsMovies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (resultsMovies[position] == null) {
            Constants.VIEW_TYPE_LOADING
        } else {
            Constants.VIEW_TYPE_ITEM
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constants.VIEW_TYPE_ITEM) {
            holder.itemView.title.text = resultsMovies[position]?.title
            holder.itemView.rating.text = resultsMovies[position]?.voteAverage.toString()
            holder.itemView.release.text = resultsMovies[position]?.releaseDate
            Picasso.get()
                .load(Constants.IMAGE_W500+resultsMovies[position]?.posterPath)
                .error(mcontext.resources.getDrawable(R.mipmap.no_image))
                .into(holder.itemView.imgBanner)
            holder.itemView.cardMovie.setOnClickListener {
                val detailsMovieIntent = Intent(mcontext, DetailsMovieActivity::class.java)
                    .apply {
                        putExtra("movieId",resultsMovies[position]?.id.toString())
                    }
                mcontext.startActivity(detailsMovieIntent)
            }
        }

        if (holder.itemViewType == Constants.VIEW_TYPE_LOADING && isLast)
            holder.itemView.progressbar.visibility = View.GONE
    }
}