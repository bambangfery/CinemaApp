package com.test.cinemaapp.data.model

import com.google.gson.annotations.SerializedName

data class ReviewMovieResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: ArrayList<ReviewMovie?>,

	@field:SerializedName("total_results")
	val totalResults: Int
)