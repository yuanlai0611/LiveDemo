package com.example.livedemo

import android.content.Context
import android.content.Context.MODE_PRIVATE

object LiveRoomManager {

    private const val LIVE_URL = "LIVE_URL"
    private const val LIVE_ROOM = "LIVE_ROOM"

    fun saveLiveUrl(ctx: Context?, url: String) {
        ctx?.apply {
            getSharedPreferences(LIVE_ROOM, MODE_PRIVATE)
                ?.edit()
                ?.putString(LIVE_URL, url)
                ?.apply()
        }
    }

    fun getLiveUrl(ctx: Context?): String {
        return ctx
            ?.getSharedPreferences(LIVE_URL, MODE_PRIVATE)
            ?.getString(LIVE_URL, "") ?: ""
    }

}