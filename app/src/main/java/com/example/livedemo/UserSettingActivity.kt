package com.example.livedemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_user_setting.*
import java.io.FileDescriptor
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.log

class UserSettingActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PICK_PIC = 0x111
        private const val TAG = "UserSettingActivity"
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)

        avatarFl.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_PICK_PIC)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_PIC && resultCode == Activity.RESULT_OK) {
            val picUri = data?.data
            picUri?.apply {
                var pfd: ParcelFileDescriptor? = null
                try {
                    pfd = contentResolver.openFileDescriptor(picUri, "r")
                    val bitmap = BitmapFactory.decodeFileDescriptor(pfd?.fileDescriptor ?: return)
                    avatarRiv.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    Log.e(TAG, e.message, e)
                } finally {
                    try {
                        pfd?.close()
                    } catch (e: IOException) {
                        Log.e(TAG, e.message, e)
                    }
                }
            }

        }
    }

}
