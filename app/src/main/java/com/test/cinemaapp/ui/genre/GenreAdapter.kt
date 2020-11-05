package com.test.cinemaapp.ui.genre

import android.content.Context
import android.content.Intent
import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.Genres
import com.test.cinemaapp.ui.movie.MovieActivity
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(var genre : ArrayList<Genres>, val context: Context) : Item<ViewHolder>()  {
    override fun getLayout(): Int {
        return R.layout.item_genre
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.genre.text = genre[position].name
        itemView.cardGenre.setOnClickListener {
            val intentMovie = Intent(context, MovieActivity::class.java).apply {
                putExtra("genreId",genre[position].id.toString())
                putExtra("genreName",genre[position].name)
            }
            context.startActivity(intentMovie)
        }
    }
}