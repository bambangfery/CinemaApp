package com.test.cinemaapp.data.model

import com.google.gson.annotations.SerializedName

data class ReviewMovie(

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("url")
	val url: String
)