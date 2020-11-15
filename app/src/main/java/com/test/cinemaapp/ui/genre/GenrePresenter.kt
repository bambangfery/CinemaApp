package com.test.cinemaapp.ui.genre

import android.content.Context
import com.test.cinemaapp.R
import com.test.cinemaapp.data.api.ApiClient
import com.test.cinemaapp.data.api.ApiServiceInterface
import com.test.cinemaapp.util.CheckConnection
import com.test.cinemaapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GenrePresenter :GenreContract.Presenter{

    private val subscriptions = CompositeDisposable()
    private lateinit var view: GenreContract.View
    private val ApiSevices: ApiServiceInterface = ApiClient.create()


    override fun getGenres(context: Context) {
        val checkConnection = CheckConnection(context)
        if (!checkConnection.isInternetAvailable()){
            view.onDomainError(context.getString(R.string.check_your_connection))
        }else{
            val subscribe = ApiSevices.getGenre(Constants.KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onDomainSuccess(it.genres)
                }, { error ->
                    var msg = error.localizedMessage
                    view.onDomainError(msg)
                })
            subscriptions.add(subscribe)
        }
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: GenreContract.View) {
        this.view = view
    }

}