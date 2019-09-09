package com.cotemig.fluo.services

import com.cotemig.fluo.models.Account
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceAccount {

    @POST("account/auth")
    fun auth(@Body account: Account): Call<Account>

    @POST("account")
    fun register(@Body account: Account): Call<Account>

    @POST("account/forgot")
    fun forgetPassword(@Body account: Account): Call<Void>

    @Multipart
    @POST("account/photo")
    fun sendPhoto(@Part image: MultipartBody.Part): Call<Account>


}