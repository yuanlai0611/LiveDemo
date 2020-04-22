package com.example.livedemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.livedemo.model.getIsLogin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = if (getIsLogin(applicationContext)) {
            Intent(this, LiveListActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
//        setContentView(R.layout.activity_main)
//
//        mRecordBtn.setOnClickListener {
//            val intent = Intent(this, RecordActivity::class.java)
//            startActivity(intent)
//        }
//
//        mPlayBtn.setOnClickListener {
//            val intent = Intent(this, PlayActivity::class.java)
//            startActivity(intent)
//        }
    }
}
