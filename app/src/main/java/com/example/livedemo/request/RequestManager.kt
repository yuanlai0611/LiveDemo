package com.example.livedemo.request

import android.content.Context
import android.util.Log
import com.example.livedemo.model.getToken
import okhttp3.*
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RequestManager {

    private const val BASE_URL = "http://120.25.229.252/"
    private const val SOCKET_URL_PATTERN = "ws://120.25.229.252:8080/websocket/{roomNum}/{userId}"
    private const val TAG = "RequestManager"

    private val client by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private val socketClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.MINUTES).build()
    }

    private val loginService by lazy {
        client.create(UserService::class.java)
    }

    private val liveRoomService by lazy {
        client.create(LiveService::class.java)
    }

    fun login(userName: String, password: String, callback: Callback<CommonRsp<String>>) {
        loginService.login(userName, password).enqueue(callback)
    }

    fun register(userName: String, password: String, roomName: String, mobile: String, callback: Callback<CommonRsp<String>>) {
        loginService.register(userName, password, roomName, mobile).enqueue(callback)
    }

    fun getUsrInfo(app: Context, callback: Callback<CommonRsp<UserRsp>>) {
        val token = getToken(app)
        if (token?.isEmpty() ?: return) {
            return
        }
        loginService.getUsrInfo(token).enqueue(callback)
    }

    fun getLiveRoomList(callback: Callback<CommonRsp<List<LiveRoomRsp>>>) {
        liveRoomService.getLiveRooms().enqueue(callback)
    }

    fun startSocket(roomNum: String, userId: String, socketListener: WebSocketListener) {
       val socketRequest = Request.Builder()
            .url((SOCKET_URL_PATTERN).replace("{roomNum}", roomNum).replace("{userId}", userId))
            .build()

        Log.d(TAG, (SOCKET_URL_PATTERN).replace("{roomNum}", roomNum).replace("{userId}", userId))
        socketClient.newWebSocket(socketRequest, socketListener)
    }


}