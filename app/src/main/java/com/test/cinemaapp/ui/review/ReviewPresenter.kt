package com.test.cinemaapp.ui.review

import com.test.cinemaapp.data.api.ApiClient
import com.test.cinemaapp.data.api.ApiServiceInterface
import com.test.cinemaapp.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ReviewPresenter : ReviewContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: ReviewContract.View
    private val ApiSevices: ApiServiceInterface = ApiClient.create()

    override fun getReview(movieId: String, page: Int) {
        val subscribe = ApiSevices.getReviewMovie(movieId, Constants.KEY,page.toString()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.totalResults>0){
                    view.onDomainSuccess(it)
                }else{
                    view.onDomainError("noTrailer")
                }
            }, { error ->
                var msg = error.localizedMessage
                view.onDomainError(msg)
            })
        subscriptions.add(subscribe)
    }

    override fun getReviewList(movieId: String, page: Int) {
        val subscribe = ApiSevices.getReviewMovie(movieId, Constants.KEY,page.toString()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.totalResults>0){
                    view.onReviewListSuccess(it.results)
                }else{
                    view.onReviewListError("noTrailer")
                }
            }, { error ->
                var msg = error.localizedMessage
                view.onReviewListError(msg)
            })
        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ReviewContract.View) {
        this.view = view
    }
}