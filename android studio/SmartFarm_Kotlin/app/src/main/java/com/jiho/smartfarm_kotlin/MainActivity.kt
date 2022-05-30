package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_main_temp = findViewById<ImageButton>(R.id.btn_main_temp)
        val btn_main_illuminance = findViewById<ImageButton>(R.id.btn_main_illuminance)
        val btn_main_soil = findViewById<ImageButton>(R.id.btn_main_soil)
        val btn_main_humidity = findViewById<ImageButton>(R.id.btn_main_humidity)
        val btn_main_cage1 = findViewById<ImageButton>(R.id.btn_main_cage1)
        val btn_main_cage2 = findViewById<ImageButton>(R.id.btn_main_cage2)
        val btn_main_cam = findViewById<ImageButton>(R.id.btn_main_cam)
        val btn_main_control = findViewById<ImageButton>(R.id.btn_main_control)
        val btn_main_info = findViewById<ImageButton>(R.id.btn_main_info)
        val img_main_weather = findViewById<ImageView>(R.id.img_main_weather)
        val img_main_cage = findViewById<ImageView>(R.id.img_main_cage)
        var weather: String? = intent.getStringExtra("weather")
        var temp: String? = intent.getStringExtra("temp")
        val text_main_tempe = findViewById<TextView>(R.id.text_main_temp)
        val text_main_date = findViewById<TextView>(R.id.text_main_date)
        var case: String? = ""
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val formatted = current.format(formatter)

        text_main_tempe.text = temp + "℃"
        text_main_date.text = "제주도\n" + formatted

        when(weather) {
            "Clear" -> img_main_weather.setImageResource(R.drawable.sunny_image)
            "Rain" -> img_main_weather.setImageResource(R.drawable.rain_image)
            "Snow" -> img_main_weather.setImageResource(R.drawable.snow_image)
            "Drizzle" -> img_main_weather.setImageResource(R.drawable.raincloud_image)
            "Clouds" -> img_main_weather.setImageResource(R.drawable.cloud_image)
            "thunderstorm" -> img_main_weather.setImageResource(R.drawable.thunder_image)
            else -> img_main_weather.setImageResource(R.drawable.sunny_image)
        }

        if(intent.hasExtra("case")) {
            case = intent.getStringExtra("case")
        } else {
            case = "case1"
        }

        btn_main_cage1.setOnClickListener {
            case = "case1"
            img_main_cage.setBackgroundResource(R.drawable.cage1_main)
            btn_main_cage1.setBackgroundResource(R.drawable.case1_push_button)
            btn_main_cage2.setBackgroundResource(R.drawable.case2_button)
        }

        btn_main_cage2.setOnClickListener {
            case = "case2"
            img_main_cage.setBackgroundResource(R.drawable.cage2_main)
            btn_main_cage2.setBackgroundResource(R.drawable.case2_push_button)
            btn_main_cage1.setBackgroundResource(R.drawable.case1_button)
        }

        btn_main_temp.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "온도")
            intent.putExtra("case" , case)
            startActivity(intent)
        }

        btn_main_illuminance.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "조도")
            intent.putExtra("case" , case)
            startActivity(intent)
        }

        btn_main_soil.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "토양")
            intent.putExtra("case" , case)
            startActivity(intent)
        }

        btn_main_humidity.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "습도")
            intent.putExtra("case", case)
            startActivity(intent)
        }

        btn_main_cam.setOnClickListener {
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
        }

        btn_main_control.setOnClickListener {
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
        }

        btn_main_info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
        }
    }
}