package com.example.livedemo.request

data class LiveRoomRsp(
    val id: Int,
    val username: String,
    val password: String,
    val roomName: String,
    val mobile: String,
    val roomNum: String
)