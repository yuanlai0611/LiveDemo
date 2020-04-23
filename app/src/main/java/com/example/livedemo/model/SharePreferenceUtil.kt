package com.example.livedemo.model

import android.content.Context
import android.content.Context.MODE_PRIVATE

private const val LIVE_DEMO = "live_demo"
private const val TOKEN = "token"
private const val IS_LOGIN = "is_login"
private const val USR_NAME = "usr_name"
private const val PHONE = "phone"
private const val USR_ID = "usr_id"
private const val ROOM_NUM = "room_num"

fun saveIsLogin(app: Context, isLogin: Boolean) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putBoolean(IS_LOGIN, isLogin).apply()
}

fun getIsLogin(app: Context): Boolean {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getBoolean(IS_LOGIN, false)
}

fun saveToken(app: Context, token: String) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putString(TOKEN, token).apply()
}

fun getToken(app: Context): String? {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getString(TOKEN, "")
}

fun saveUserName(app: Context, name: String) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putString(USR_NAME, name)
        .apply()
}

fun getUsrName(app: Context): String? {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getString(USR_NAME, "")
}

fun savePhone(app: Context, phone: String) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putString(PHONE, phone)
        .apply()
}

fun getPhone(app: Context): String? {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getString(PHONE, "")
}

fun saveUsrId(app: Context, usrId: String) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putString(USR_ID, usrId)
        .apply()
}

fun getUsrId(app: Context): String? {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getString(USR_ID, "")
}

fun saveRoomNum(app: Context, roomNum: String) {
    app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .edit().putString(ROOM_NUM, roomNum)
        .apply()
}

fun getRoomNum(app: Context): String? {
    return app.getSharedPreferences(LIVE_DEMO, MODE_PRIVATE)
        .getString(ROOM_NUM, "")
}