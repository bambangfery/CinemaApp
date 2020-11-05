package com.test.cinemaapp.ui.detailsmovie

import com.test.cinemaapp.R
import com.test.cinemaapp.data.model.Genres
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_list_genre.view.*

class GenreListAdapter(var genre : List<Genres>) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_list_genre
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.genreName.text = genre[position].name
    }
}