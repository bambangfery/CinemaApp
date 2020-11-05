package com.test.cinemaapp.ui.movie

import com.test.cinemaapp.data.api.ApiClient
import com.test.cinemaapp.data.api.ApiServiceInterface
import com.test.cinemaapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviePresenter : MovieContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: MovieContract.View
    private val ApiSevices: ApiServiceInterface = ApiClient.create()

    override fun getMovie(genreId : String, page : Int) {
        val subscribe = ApiSevices.getResultsMovie(Constants.KEY,genreId,page.toString()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onDomainSuccess(it)
            }, { error ->
                var msg = error.localizedMessage
                view.onDomainError(msg)
            })
        subscriptions.add(subscribe)
    }

    override fun getMovieList(genreId: String, page: Int) {
        val subscribe = ApiSevices.getResultsMovie(Constants.KEY,genreId,page.toString()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onMovieListSuccess(it.results)
            }, { error ->
                var msg = error.localizedMessage
                view.onMovieListError(msg)
            })
        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MovieContract.View) {
        this.view = view
    }
}