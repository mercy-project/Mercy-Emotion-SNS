package com.content.mercy.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("/sentiment/{sentence}")
    fun listRepos(): Call<Repo>


}