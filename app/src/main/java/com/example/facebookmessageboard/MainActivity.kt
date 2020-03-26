package com.example.facebookmessageboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.facebookmessageboard.api.API
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRegister.setOnClickListener{
            if (!edtUser.text.isNullOrEmpty()) {
                if (!edtPassword.text.isNullOrEmpty()) {
                    register(this, edtUser.text.toString(), edtPassword.text.toString())
                } else {
                    Toast.makeText(this, "請輸入註冊密碼", Toast.LENGTH_LONG).show()
                }
            } else Toast.makeText(this, "請輸入註冊帳號", Toast.LENGTH_LONG).show()
        }

        btnLogin.setOnClickListener{
            if (!edtUser.text.isNullOrEmpty()) {
                if (!edtPassword.text.isNullOrEmpty()) {
                    login(this, edtUser.text.toString(), edtPassword.text.toString())
                } else {
                    Toast.makeText(this, "請輸入登入密碼", Toast.LENGTH_LONG).show()
                }
            } else Toast.makeText(this, "請輸入登入帳號", Toast.LENGTH_LONG).show()
        }

        tvEntry.setOnClickListener {
            API.token = null
            val intnet = Intent(this, MessageBoardActivity::class.java)
            startActivity(intnet)

        }


    }

    private fun register(context: Context, user: String, password: String) {
        API.apiInterface.register(RegisterRequest(user, password)).enqueue(object : Callback<RegisterResponse>{
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "恭喜註冊成功", Toast.LENGTH_LONG).show()
                } else if (response.code() == 400) {
                    Toast.makeText(context, "使用者不存在或密碼不對喔！", Toast.LENGTH_LONG).show()
                } else if (response.code() == 416) {
                    Toast.makeText(context, "此帳號已被註冊過了喔！", Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    private fun login(context: Context, user: String, password: String) {
        API.apiInterface.login(LoginRequest(user, password)).enqueue(object :
            Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responsebody = response.body()
                    val token = responsebody!!.msg
                    API.token = token
                    println("=========token=${API.token}")
                    val intnet = Intent(context, MessageBoardActivity::class.java)
                    startActivity(intnet)
                    Toast.makeText(context, "恭喜登入成功", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
