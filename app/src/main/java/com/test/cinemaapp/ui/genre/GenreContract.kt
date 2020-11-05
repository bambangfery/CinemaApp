package com.test.cinemaapp.ui.genre

import com.test.cinemaapp.data.model.Genres
import com.test.cinemaapp.ui.base.BaseContract


class GenreContract {

    interface View: BaseContract.View{

        fun onDomainSuccess(genres: ArrayList<Genres>)
        fun onDomainError(msg: String)

    }

    interface Presenter:BaseContract.Presenter<View>{
        fun getGenres()
    }
}