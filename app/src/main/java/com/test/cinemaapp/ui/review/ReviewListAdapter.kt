package com.test.cinemaapp.ui.review

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.ReviewMovie
import com.test.cinemaapp.util.Constants
import kotlinx.android.synthetic.main.item_list_review.view.*
import kotlinx.android.synthetic.main.progress_loading.view.*

class ReviewListAdapter(private var resultsReview: ArrayList<ReviewMovie?>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var mcontext: Context
    var isLast: Boolean = false

    class ReviewMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<ReviewMovie?>) {
        this.resultsReview.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            resultsReview.add(null)
            notifyItemInserted(resultsReview.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (resultsReview.size != 0) {
            resultsReview.removeAt(resultsReview.size - 1)
            notifyItemRemoved(resultsReview.size)
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
                    R.layout.item_list_review
                    , parent, false)
            ReviewMoviesViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(
                    R.layout.progress_loading
                    , parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return resultsReview.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (resultsReview[position] == null) {
            Constants.VIEW_TYPE_LOADING
        } else {
            Constants.VIEW_TYPE_ITEM
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constants.VIEW_TYPE_ITEM) {
            holder.itemView.author.text = resultsReview[position]?.author
            holder.itemView.content.text = resultsReview[position]?.content
        }

        if (holder.itemViewType == Constants.VIEW_TYPE_LOADING && isLast)
            holder.itemView.progressbar.visibility = View.GONE
    }
}