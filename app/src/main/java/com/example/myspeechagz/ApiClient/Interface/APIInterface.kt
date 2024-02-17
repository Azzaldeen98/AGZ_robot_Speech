package com.example.myspeechagz.ApiClient.Interface

import com.example.myspeechagz.Models.RequestModels.GeminiRequest
import com.example.myspeechagz.Models.ResponseModels.GeminiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {

    @Headers("Content-Type: application/json",/*"User-Agent: Retrofit-Sample-App"*/)

//    @GET("users/{id}")
//    fun getUser(@Path("id") userId: String): Call<User>

    @POST("/todos/api")
    fun generateAiAudio(@Body body: GeminiRequest): Call<GeminiResponse?>?

    @POST("/todos/api/text")
    fun generateAiText(@Body body: GeminiRequest): Call<GeminiResponse?>?

//    @FormUrlEncoded
//    @POST("user/edit")
//    fun generateText( @Field("first_name")_content: String? ): Call<GeminiResponse?>?


}