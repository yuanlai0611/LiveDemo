package com.example.livedemo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.item_bullet.view.*

class PlayActivity : AppCompatActivity() {

    companion object {
        const val STREAM_URL = "http://120.25.229.252/hls/test2.m3u8"

        const val REQUEST_PLAY = 0x111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        initMediaView()

        val bulletDataItems = mutableListOf<BulletDataItem>()
        repeat(30) {
            bulletDataItems.add(BulletDataItem())
        }
        val bulletAdapter = BulletAdapter(this, bulletDataItems)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = VERTICAL
        bulletRv.layoutManager = layoutManager
        bulletRv.adapter = bulletAdapter
    }

//    @AfterPermissionGranted(REQUEST_PLAY)
//    private fun requestPermissions() {
//        val permissons = arrayOf()
//        if (EasyPermissions.hasPermissions())
//    }

    override fun onResume() {
        super.onResume()
        livePlv.start()
    }

    override fun onPause() {
        super.onPause()
        livePlv.stopPlayback()
    }

    private fun initMediaView() {
        livePlv.setVideoPath(STREAM_URL)
    }
}

class BulletAdapter(private val ctx: Context, private val bulletDataItems: List<BulletDataItem>) :
    RecyclerView.Adapter<BulletViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BulletViewHolder {
        return BulletViewHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.item_bullet,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return bulletDataItems.size
    }

    override fun onBindViewHolder(holder: BulletViewHolder, position: Int) {
        holder.bind(bulletDataItems[position])
    }
}

class BulletViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
    fun bind(dataItem: BulletDataItem) {
        v.usrTv.text = dataItem.usrName
        v.msgTv.text = dataItem.msg
    }
}
