package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    val host:String = "172.20.10.3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value)

        val layout_value_value = findViewById<ConstraintLayout>(R.id.layout_valu_value)
        val btn_value_cage1 = findViewById<ImageButton>(R.id.btn_value_cage1)
        val btn_value_cage2 = findViewById<ImageButton>(R.id.btn_value_cage2)
        val btn_value_info = findViewById<ImageButton>(R.id.btn_value_info)
        val img_value_casenumber = findViewById<ImageView>(R.id.img_value_casenumber)
        val img_value_value = findViewById<ImageView>(R.id.img_value_value)
        val btn_value_back = findViewById<ImageButton>(R.id.btn_value_back)
        case = intent.getStringExtra("case")
        value = intent.getStringExtra("value")

        when (value) {
            "온도" -> {
                value1 = "temp"
                img_value_value.setBackgroundResource(R.drawable.temptable_text_image)
                layout_value_value.setBackgroundResource(R.drawable.temperature_table)
            }
            "조도" -> {
                value1 = "illuminance"
                img_value_value.setBackgroundResource(R.drawable.illuminancetable_text_image)
                layout_value_value.setBackgroundResource(R.drawable.illuminance_table)
            }
            "토양" -> {
                value1 = "water"
                img_value_value.setBackgroundResource(R.drawable.soiltable_text_image)
                layout_value_value.setBackgroundResource(R.drawable.soil_table)
            }
            "습도" -> {
                value1 = "moisture"
                img_value_value.setBackgroundResource(R.drawable.humiditytable_text_image)
                layout_value_value.setBackgroundResource(R.drawable.humidity_table)
            }
            else -> {
                value = "온도"
                value1 = "temp"
                img_value_value.setBackgroundResource(R.drawable.temptable_text_image)
                layout_value_value.setBackgroundResource(R.drawable.temperature_table)
            }
        }

        when (case) {
            "case1" -> {
                img_value_casenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_value_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_value_cage2.setBackgroundResource(R.drawable.case2_button)
            }
            "case2" -> {
                img_value_casenumber.setBackgroundResource(R.drawable.cage2_main)
                btn_value_cage1.setBackgroundResource(R.drawable.case1_button)
                btn_value_cage2.setBackgroundResource(R.drawable.case2_push_button)
            }
            else -> {
                img_value_casenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_value_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_value_cage2.setBackgroundResource(R.drawable.case2_button)
            }
        }

        btn_value_back.setOnClickListener{
            finish()
        }

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
        }

        val thread = NetworkThread() //데이터 사용 시 튕기는 현상 발생 수정하기 위해서는 코루틴 코드로 바꿔야함
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

                if (str != null) {
                    buf.append(str)
                }
            } while (str != null)

            val root = JSONObject(buf.toString())
            val result = root.getJSONArray("result")

            runOnUiThread {
                for (i in 0 until result.length()) {
                    val jObject = result.getJSONObject(i)
                    date = JSON_Parse(jObject, "date")
                    value2 = JSON_Parse(jObject, "${value1}")
                    list.add(RecycleData(date!!, value2!!))

                    Review = findViewById(R.id.reView)
                    Review!!.layoutManager = LinearLayoutManager(
                        this@ValueActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )

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