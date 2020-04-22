package com.example.livedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import com.example.livedemo.model.saveIsLogin
import com.example.livedemo.model.saveToken
import com.example.livedemo.model.saveUserName
import com.example.livedemo.request.CommonRsp
import com.example.livedemo.request.LoginRsp
import com.example.livedemo.request.RequestManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var isLogin = true

    companion object {
        private const val IS_LOGIN = "is_login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        switchLogin(true)
        switchRiv.setOnClickListener {
            isLogin = !isLogin
            switchLogin(isLogin)
        }
        actionBtn.setOnClickListener {
            if (isLogin) {
                login()
            } else {
                register()
            }
        }
    }

    private fun login(usrName: String?, password: String?) {
        RequestManager.login(usrName ?: return, password ?: return, object : Callback<CommonRsp<String>> {
            override fun onFailure(call: Call<CommonRsp<String>>, t: Throwable) {
                errorToast("登录失败")
            }

            override fun onResponse(
                call: Call<CommonRsp<String>>,
                response: Response<CommonRsp<String>>
            ) {
                if (response.body()?.status == 0) {
                    jumpToLiveListActivity()
                    saveToken(this@LoginActivity, response.body()?.data ?: "")
                    saveUserName(this@LoginActivity, usrName)
                } else {
                    errorToast("登录失败")
                }
            }
        })
    }

    private fun login() {
        val usrName = usrNameEt.text?.toString()
        val password = passwordEt.text?.toString()
        login(usrName, password)
    }

    private fun register() {
        val usrName = usrNameEt.text?.toString()
        val password = passwordEt.text?.toString()
        val roomName = roomNameEt.text?.toString()
        val phone = phoneEt.text?.toString()
        RequestManager.register(usrName ?: return, password ?: return, roomName ?: return, phone ?: return, object : Callback<CommonRsp<String>> {
            override fun onFailure(call: Call<CommonRsp<String>>, t: Throwable) {
                errorToast("注册失败")
            }

            override fun onResponse(call: Call<CommonRsp<String>>, response: Response<CommonRsp<String>>) {
                if (response.body()?.status == 0) {
                    login(usrName, password)
                } else {
                    errorToast("注册失败")
                }
            }
        })
    }

    private fun errorToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun jumpToLiveListActivity() {
        val intent = Intent(this, LiveListActivity::class.java)
        startActivity(intent)
        saveIsLogin(this, true)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean(IS_LOGIN, isLogin)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isLogin = savedInstanceState.getBoolean(IS_LOGIN, false)
    }

    private fun switchLogin(isLogin: Boolean) {
        roomNameTIL.visibility = if (isLogin) View.GONE else View.VISIBLE
        phoneTIL.visibility = if (isLogin) View.GONE else View.VISIBLE
        title = if (isLogin) "登录" else "注册"
        usrNameEt.setText("")
        passwordEt.setText("")
        roomNameEt.setText("")
        phoneEt.setText("")
    }

}
