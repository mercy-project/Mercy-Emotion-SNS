package com.content.mercy.retrofit

import com.google.gson.annotations.SerializedName

// 감정 데이터 클래스
data class Repo(
    @SerializedName("sentiment")
    val sentiment: String,
    @SerializedName("sentence")
    val sentence: String
)