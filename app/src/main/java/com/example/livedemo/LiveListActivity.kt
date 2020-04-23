package com.example.livedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.livedemo.model.getPhone
import com.example.livedemo.model.savePhone
import com.example.livedemo.model.saveRoomNum
import com.example.livedemo.model.saveUsrId
import com.example.livedemo.request.CommonRsp
import com.example.livedemo.request.LiveRoomRsp
import com.example.livedemo.request.RequestManager
import com.example.livedemo.request.UserRsp
import kotlinx.android.synthetic.main.activity_live_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveListActivity : AppCompatActivity() {

    private val liveDataItem = mutableListOf<LiveDataItem>()
    private var liveListAdapter: LiveListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_list)
        title = "直播列表"
        liveListAdapter = LiveListAdapter(this, liveDataItem)
        liveListAdapter?.setOnItemClickListener {
            if (getPhone(this) == liveDataItem[it].phone) {
                Toast.makeText(this, "不能进自己的直播间", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }
            PlayActivity.startActivity(this, liveDataItem[it].roomNum, liveDataItem[it].usrId, liveDataItem[it].usrName)
        }
        liveListRiv.layoutManager = GridLayoutManager(this, 2)
        liveListRiv.adapter = liveListAdapter

        liveBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }

        RequestManager.getUsrInfo(this, object : Callback<CommonRsp<UserRsp>> {
            override fun onFailure(call: Call<CommonRsp<UserRsp>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CommonRsp<UserRsp>>,
                response: Response<CommonRsp<UserRsp>>
            ) {
                savePhone(this@LiveListActivity, response.body()?.data?.mobile ?: return)
                saveUsrId(this@LiveListActivity, response.body()?.data?.id ?: return)
                saveRoomNum(this@LiveListActivity, response.body()?.data?.roomNum ?: return)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        RequestManager.getLiveRoomList(object : Callback<CommonRsp<List<LiveRoomRsp>>> {
            override fun onFailure(call: Call<CommonRsp<List<LiveRoomRsp>>>, t: Throwable) {
                Toast.makeText(this@LiveListActivity, "拉取房间信息失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CommonRsp<List<LiveRoomRsp>>>,
                response: Response<CommonRsp<List<LiveRoomRsp>>>
            ) {
                response.body()?.data?.also {
                    liveDataItem.clear()
                }?.forEach {
                    liveDataItem.add(
                        LiveDataItem(
                            phone = it.mobile,
                            title = it.roomName,
                            usrName = it.username,
                            usrId = it.id,
                            roomNum = it.roomNum
                        )
                    )
                    liveListAdapter?.setData(liveDataItem)
                }
            }
        })
    }
}
