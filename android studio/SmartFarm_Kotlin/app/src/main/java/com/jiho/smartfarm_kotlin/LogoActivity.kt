package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class LogoActivity : AppCompatActivity() {

    var wt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        val thread = NetworkThread()
        thread.start()
        thread.join()

        startLoading()
    }

    inner class NetworkThread: Thread() {
        override fun run() {

            val site = "http://api.openweathermap.org/data/2.5/weather?lat=33.38&lon=126.60&appid=842fd2fda672b3b53b2c831970e7aa33"

            val url = URL(site)
            val conn = url.openConnection()
            val input = conn.getInputStream()
            val isr = InputStreamReader(input)
            val br = BufferedReader(isr)

            var str: String?
            val buf = StringBuffer()

            do {
                str = br.readLine()

                if(str != null) {
                    buf.append(str)
                }
            } while(str != null)

            val root = JSONObject(buf.toString())
            val weather = root.getJSONArray("weather")

            runOnUiThread {

                val jObject = weather.getJSONObject(0)
                wt = JSON_Parse(jObject, "main")
            }
        }

        fun JSON_Parse(obj:JSONObject, data : String): String {
            return try {
                obj.getString(data)
            } catch (e: Exception) {
                "없습니다."
            }
        }
    }

    private fun startLoading() {  // 딜레이주고 메인 엑티비티로 넘어가기 여기서 조건 주어서 처음 접속이면 스타팅 화면1 로
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.putExtra("cage", "cage1")
            intent.putExtra("weather", wt)
            startActivity(intent)
            finish()
        }, 2000)
    }
}