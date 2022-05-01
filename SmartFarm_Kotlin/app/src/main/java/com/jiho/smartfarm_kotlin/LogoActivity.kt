package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        startLoading()
    }
    private fun startLoading() {  // 딜레이주고 메인 엑티비티로 넘어가기 여기서 조건 주어서 처음 접속이면 스타팅 화면1 로
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.putExtra("cage", "cage1")
            startActivity(intent)
            finish()
        }, 2000)
    }
}