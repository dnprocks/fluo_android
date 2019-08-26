package com.cotemig.fluo.services

import com.cotemig.fluo.models.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServiceAccount {

    @POST("account/auth")
    fun auth(@Body account: Account): Call<Account>

    @POST("account")
    fun register(@Body account: Account): Call<Account>

    @POST("account/forgot")
    fun forgetPassword(@Body account: Account): Call<Void>



}