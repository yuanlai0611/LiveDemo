package com.example.livedemo.request

import retrofit2.Call
import retrofit2.http.GET

interface LiveService {

    @GET("origin/live/list")
    fun getLiveRooms(): Call<CommonRsp<List<LiveRoomRsp>>>

}