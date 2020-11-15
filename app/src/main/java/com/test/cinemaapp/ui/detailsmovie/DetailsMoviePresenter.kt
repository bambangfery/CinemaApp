package com.test.cinemaapp.ui.detailsmovie

import android.content.Context
import com.test.cinemaapp.R
import com.test.cinemaapp.data.api.ApiClient
import com.test.cinemaapp.data.api.ApiServiceInterface
import com.test.cinemaapp.util.CheckConnection
import com.test.cinemaapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsMoviePresenter : DetailsMovieContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: DetailsMovieContract.View
    private val ApiSevices: ApiServiceInterface = ApiClient.create()

    override fun getDetailsMovie(context: Context, movieId: String) {
        val checkConnection = CheckConnection(context)
        if (!checkConnection.isInternetAvailable()){
            view.onDomainError(context.getString(R.string.check_your_connection))
        }else{
            val subscribe = ApiSevices.getDetailsMovie(movieId, Constants.KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onDomainSuccess(it)
                }, { error ->
                    var msg = error.localizedMessage
                    view.onDomainError(msg)
                })
            subscriptions.add(subscribe)
        }
    }

    override fun getTrailersMovie(context: Context, movieId: String) {
        val checkConnection = CheckConnection(context)
        if (!checkConnection.isInternetAvailable()){
            view.onTrailerError(context.getString(R.string.check_your_connection))
        }else{
            val subscribe = ApiSevices.getTrailerMovie(movieId, Constants.KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.size>0){
                        view.onTrailerSuccess(it)
                    }else{
                        view.onTrailerError("noTrailer")
                    }
                }, { error ->
                    var msg = error.localizedMessage
                    view.onTrailerError(msg)
                })
            subscriptions.add(subscribe)
        }

    }

    override fun getReviewMovie(context: Context, movieId: String) {
        val checkConnection = CheckConnection(context)
        if (!checkConnection.isInternetAvailable()){
            view.onDomainError(context.getString(R.string.check_your_connection))
        }else{
            val subscribe = ApiSevices.getReviewMovie(movieId, Constants.KEY,"1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.totalResults>0){
                        view.onReviewSuccess(it)
                    }else{
                        view.onReviewError("noReview")
                    }
                }, { error ->
                    var msg = error.localizedMessage
                    view.onReviewError(msg)
                })
            subscriptions.add(subscribe)
        }
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: DetailsMovieContract.View) {
        this.view = view
    }
}