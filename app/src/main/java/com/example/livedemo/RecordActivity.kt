package com.example.livedemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter
import kotlinx.android.synthetic.main.activity_record.*
import me.lake.librestreaming.core.listener.RESConnectionListener
import me.lake.librestreaming.filter.hardvideofilter.HardVideoGroupFilter
import me.lake.librestreaming.ws.StreamAVOption
import me.lake.librestreaming.ws.filter.hardfilter.GPUImageBeautyFilter
import me.lake.librestreaming.ws.filter.hardfilter.extra.GPUImageCompatibleFilter
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class RecordActivity : AppCompatActivity(), RESConnectionListener,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val STREAM_URL = "rtmp://120.25.229.252:1935/hls/test2"
        const val TAG = "RecordActivity"
        const val REQUEST_CAMERA = 0x111
    }

    // 是否是主镜头
    private var isMasterCamera = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
        liveUrlEt.setText(LiveRoomManager.getLiveUrl(this))
    }

    override fun onPause() {
        super.onPause()
        liveCv.destroy()
        LiveRoomManager.saveLiveUrl(this, liveUrlEt.text.toString())
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
        streamAVOptions.streamUrl = STREAM_URL

        liveCv.init(this, streamAVOptions)
        liveCv.addStreamStateListener(this)

        val filters = listOf(
            GPUImageCompatibleFilter(GPUImageBeautyFilter()),
            GPUImageCompatibleFilter(GPUImageAddBlendFilter())
        )

        liveCv.setHardVideoFilter(HardVideoGroupFilter(filters))
        swapBtn.setOnClickListener {
            liveCv.swapCamera()
        }
        startLiveBtn.setOnClickListener {
            liveCv.startStreaming(STREAM_URL)
        }
        liveUrlEt.addTextChangedListener { e ->
            run {
                LiveRoomManager.saveLiveUrl(this, e.toString())
            }
        }
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
}
