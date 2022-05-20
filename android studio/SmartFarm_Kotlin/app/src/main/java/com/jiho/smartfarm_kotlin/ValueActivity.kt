package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class ValueActivity : AppCompatActivity() {

    private var Adapter: RecycleAdapter? = null
    private var Review: RecyclerView? = null
    var case:String? = ""
    var value:String? = ""
    var value1:String? =""
    var value2:String? = ""
    var date:String? = ""
    val list:MutableList<RecycleData> = mutableListOf()
    val host:String = "192.168.4.64"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value)

        val text_value_value = findViewById<TextView>(R.id.text_value_value)
        val btn_value_cage1 = findViewById<Button>(R.id.btn_value_cage1)
        val btn_value_cage2 = findViewById<Button>(R.id.btn_value_cage2)
        val btn_value_info = findViewById<Button>(R.id.btn_value_info)
        val text_value_cagenumber = findViewById<TextView>(R.id.text_value_cagenumber)
        val img_value_value = findViewById<TextView>(R.id.img_value_value)
        case = intent.getStringExtra("case")
        value = intent.getStringExtra("value")
        when(value){
            "온도" -> {
                value1 = "temp"
                img_value_value.text = "temp"
            }
            "조도" -> {
                value1 = "illuminance"
                img_value_value.text = "illuminance"
            }
            "수분" -> {
                value1 = "water"
                img_value_value.text = "water"
            }
            "습도" -> {
                value1 = "moisture"
                img_value_value.text = "moisture"
            }
        }

        text_value_cagenumber.text = case

        text_value_value.text = value

        btn_value_cage1.setOnClickListener {
            case = "case1"
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("case", case)
            intent.putExtra("value", value)
            startActivity(intent)
            finish()
        }

        btn_value_cage2.setOnClickListener {
            case = "case2"
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("case", case)
            intent.putExtra("value", value)
            startActivity(intent)
            finish()
        }

        btn_value_info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        val thread = NetworkThread()
        thread.start()
        thread.join()
    }

    inner class NetworkThread: Thread() {
        override fun run() {

            val site = "http://${host}/json${case}.php"

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
            val result = root.getJSONArray("result")

            runOnUiThread {
                for(i in 0 until result.length()) {
                    val jObject = result.getJSONObject(i)
                    date = JSON_Parse(jObject, "date")
                    value2 = JSON_Parse(jObject, "${value1}")
                    list.add(RecycleData(date!!, value2!!))

                    Review = findViewById(R.id.reView)
                    Review!!.layoutManager = LinearLayoutManager(this@ValueActivity, LinearLayoutManager.VERTICAL, false)

                    Adapter = RecycleAdapter(list)
                    Review!!.adapter = Adapter
                }
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
}