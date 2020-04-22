package com.example.livedemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.livedemo.model.getUsrId
import com.example.livedemo.model.getUsrName
import com.example.livedemo.request.BulletMsg
import com.example.livedemo.request.RequestManager
import com.google.gson.Gson
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter
import kotlinx.android.synthetic.main.activity_record.*
import me.lake.librestreaming.core.listener.RESConnectionListener
import me.lake.librestreaming.filter.hardvideofilter.HardVideoGroupFilter
import me.lake.librestreaming.ws.StreamAVOption
import me.lake.librestreaming.ws.filter.hardfilter.GPUImageBeautyFilter
import me.lake.librestreaming.ws.filter.hardfilter.extra.GPUImageCompatibleFilter
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.random.Random

class RecordActivity : AppCompatActivity(), RESConnectionListener,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val STREAM_URL = "rtmp://120.25.229.252:1935/hls/{ROOM_NUM}"
        const val TAG = "RecordActivity"
        const val REQUEST_CAMERA = 0x111
    }

    private var roomNum: String? = null
    private var socket: WebSocket? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "我的直播间"
        setContentView(R.layout.activity_record)
        roomNum = generateRandomRoomNum()
        RequestManager.startSocket(roomNum ?: return, getUsrId(this).toString(), socketListener)
        swapBtn.setOnClickListener {
            liveCv.swapCamera()
        }

        sendBtn.setOnClickListener {
            val msg = bulletEt.text?.toString()
            if (!(msg?.isEmpty() ?: return@setOnClickListener)) {
                val bulletMsg = Gson().toJson(BulletMsg(getUsrName(this@RecordActivity) ?: return@setOnClickListener, msg))
                socket?.send(bulletMsg)
                bulletEt.setText("")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requestPermission()
        bulletEt.setText(LiveRoomManager.getLiveUrl(this))
    }

    override fun onPause() {
        super.onPause()
        liveCv.destroy()
        LiveRoomManager.saveLiveUrl(this, bulletEt.text.toString())
    }

    @AfterPermissionGranted(REQUEST_CAMERA)
    private fun requestPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(this, "直播需要的权限", REQUEST_CAMERA, *permissions)
        } else {
            initLiveConfig()
        }
    }

    private fun initLiveConfig() {
        val streamAVOptions = StreamAVOption()
        streamAVOptions.streamUrl = STREAM_URL.replace("{ROOM_NUM}", roomNum ?: return)

        liveCv.init(this, streamAVOptions)
        liveCv.addStreamStateListener(this)

        val filters = listOf(
            GPUImageCompatibleFilter(GPUImageBeautyFilter()),
            GPUImageCompatibleFilter(GPUImageAddBlendFilter())
        )

        liveCv.setHardVideoFilter(HardVideoGroupFilter(filters))

    }

    override fun onOpenConnectionResult(p0: Int) {
        Log.d(TAG, "onOpenConnectionResult -> $p0")
    }

    override fun onWriteError(p0: Int) {
        Log.d(TAG, "onWriteError -> $p0")
    }

    override fun onCloseConnectionResult(p0: Int) {
        Log.d(TAG, "onCloseConnectionResult -> $p0")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun generateRandomRoomNum(): String {
        return "room${Random.nextInt(1000)}"
    }

    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            socket = webSocket
        }
    }
}
