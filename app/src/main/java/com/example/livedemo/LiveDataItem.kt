package com.example.livedemo

/**
 * 与首页列表对应
 */
data class LiveDataItem(
    val coverUrl: String = "",
    val type: String = "视频聊天",
    val num: Int = 1000,
    val avatarUrl: String = "",
    val usrName: String = "TESTV官方频道",
    val title: String = "TESTV一起聊聊天吃吃面",
    val phone: String = "",
    val usrId: Int = 0,
    val roomNum: String = ""
)