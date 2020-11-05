package com.test.cinemaapp.data.api

import com.test.cinemaapp.data.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("genre/movie/list")
    fun getGenre(
        @Query("api_key") key: String
    ) : Observable<GenresResponse>

    @GET("discover/movie")
    fun getResultsMovie(
        @Query("api_key") key: String,
        @Query("with_genres") genre: String,
        @Query("page") page: String
    ) : Observable<ResultsMoviesResponse>

    @GET("movie/{id_movie}")
    fun getDetailsMovie(
        @Path("id_movie") id: String,
        @Query("api_key") key: String
    ) : Observable<DetailsMovieResponse>

    @GET("movie/{id_movie}/videos")
    fun getTrailerMovie(
        @Path("id_movie") id: String,
        @Query("api_key") key: String
    ) : Observable<TrailerMovieResponse>

    @GET("movie/{id_movie}/reviews")
    fun getReviewMovie(
        @Path("id_movie") id: String,
        @Query("api_key") key: String,
        @Query("page") page: String
    ) : Observable<ReviewMovieResponse>

}
