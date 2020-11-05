package com.test.cinemaapp.data.model

import com.google.gson.annotations.SerializedName

data class Genres(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String

)
