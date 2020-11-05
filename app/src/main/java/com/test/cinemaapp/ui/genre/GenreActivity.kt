package com.test.cinemaapp.ui.genre

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.Genres
import com.test.cinemaapp.di.component.DaggerActivityComponent
import com.test.cinemaapp.di.module.ActivityModule
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class GenreActivity : AppCompatActivity(), GenreContract.View {
    @Inject
    lateinit var presenter: GenreContract.Presenter
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()
        presenter.attach(this)
        progressbar.visibility = View.VISIBLE
        presenter.getGenres()
    }

    override fun onDomainSuccess(genres: ArrayList<Genres>) {
        progressbar.visibility = View.GONE
        adapter.apply {
            clear()
            notifyDataSetChanged()
        }
        genres.forEach {
            adapter.add(GenreAdapter(genres,this))
        }
        genreRv.adapter = adapter
    }

    override fun onDomainError(msg: String) {
        progressbar.visibility = View.GONE
        Log.d("Error : ", msg)
    }


    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.injectMain(this)
    }
}