package com.example.livedemo.request

data class CommonRsp<T> (
    val msg: String,
    val status: Int,
    val data: T
)