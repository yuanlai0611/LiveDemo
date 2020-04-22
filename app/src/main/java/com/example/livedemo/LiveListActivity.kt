package com.example.livedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_live_list.*

class LiveListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_list)

        val liveDataItem = mutableListOf<LiveDataItem>()
        repeat(20) {
            liveDataItem.add(LiveDataItem())
        }
        val liveListAdapter = LiveListAdapter(this, liveDataItem)
        liveListRiv.layoutManager = GridLayoutManager(this, 2)
        liveListRiv.adapter = liveListAdapter

        liveBtn.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }
    }

}
