package com.example.livedemo.request

import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("origin/login")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<CommonRsp<String>>

    @FormUrlEncoded
    @POST("origin/register")
    fun register(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("roomName") roomName: String,
        @Field("mobile") mobile: String
    ): Call<CommonRsp<String>>

    @GET("origin/userinfo")
    fun getUsrInfo(
        @Header("token") token: String
    ): Call<CommonRsp<UserRsp>>

}