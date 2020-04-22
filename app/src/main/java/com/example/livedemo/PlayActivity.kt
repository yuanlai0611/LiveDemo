package com.example.livedemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.example.livedemo.model.getUsrName
import com.example.livedemo.request.BulletMsg
import com.example.livedemo.request.RequestManager
import com.google.gson.Gson
import com.pili.pldroid.player.widget.PLVideoView
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.item_bullet.view.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class PlayActivity : AppCompatActivity() {

    companion object {
        const val STREAM_URL = "http://120.25.229.252/hls/{ROOM_NUM}.m3u8"
//        const val REQUEST_PLAY = 0x111
        private const val ROOM_NUM = "room_num"
        private const val USR_ID = "usr_id"
        private const val USR_NAME = "usr_name"

        fun startActivity(ctx: Context, roomNum: String, usrId: Int, usrName: String) {
            val intent = Intent(ctx, PlayActivity::class.java)
            intent.putExtra(ROOM_NUM, roomNum)
            intent.putExtra(USR_ID, usrId)
            intent.putExtra(USR_NAME, usrName)
            ctx.startActivity(intent)
        }
    }

    private var roomNum: String? = null
    private var usrId: Int? = null
    private var usrName: String? = null
    private var socket: WebSocket? = null
    private val bulletDataItems = mutableListOf<BulletDataItem>()
    private var bulletAdapter: BulletAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        roomNum = intent.getStringExtra(ROOM_NUM)
        usrId = intent.getIntExtra(USR_ID, -1)
        usrName = intent.getStringExtra(USR_NAME)
        title = "${usrName}的直播间"
        RequestManager.startSocket(roomNum ?: return, usrId.toString(), webSocketListener)

        initMediaView(STREAM_URL.replace("{ROOM_NUM}", roomNum ?: return))

        bulletAdapter = BulletAdapter(this, bulletDataItems)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = VERTICAL
        bulletRv.layoutManager = layoutManager
        bulletRv.adapter = bulletAdapter

        sendBtn.setOnClickListener {
            val msg = bulletEt.text?.toString()
            if(!(msg?.isEmpty() ?: return@setOnClickListener)) {
                bulletEt.setText("")
                val bulletMsg = Gson().toJson(BulletMsg(getUsrName(this@PlayActivity) ?: return@setOnClickListener, msg))
                socket?.send(bulletMsg)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        livePlv.start()
    }

    override fun onPause() {
        super.onPause()
        livePlv.stopPlayback()
    }

    private fun initMediaView(streamUrl: String) {
        livePlv.setVideoPath(streamUrl)
        livePlv.displayAspectRatio = PLVideoView.ASPECT_RATIO_PAVED_PARENT
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            val bulletMsg = Gson().fromJson<BulletMsg>(text, BulletMsg::class.java)
            bulletDataItems.add(BulletDataItem(usrName = bulletMsg.name, msg = bulletMsg.msg))
            bulletAdapter?.notifyDataSetChanged()
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            socket = webSocket
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.cancel()
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
