package com.content.mercy.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("/sentiment")
    fun listRepos(@Query("sentence") sentence: String): Call<Repo>
}