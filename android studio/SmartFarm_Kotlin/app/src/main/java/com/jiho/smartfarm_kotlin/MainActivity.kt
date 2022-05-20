package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_main_temp = findViewById<Button>(R.id.btn_main_temp)
        val btn_main_light = findViewById<Button>(R.id.btn_main_light)
        val btn_main_moisture = findViewById<Button>(R.id.btn_main_moisture)
        val btn_main_humidity = findViewById<Button>(R.id.btn_main_humidity)
        val btn_main_cage1 = findViewById<Button>(R.id.btn_main_cage1)
        val btn_main_cage2 = findViewById<Button>(R.id.btn_main_cage2)
        val btn_main_cam = findViewById<Button>(R.id.btn_main_cam)
        val btn_main_control = findViewById<Button>(R.id.btn_main_control)
        val btn_main_info = findViewById<Button>(R.id.btn_main_info)
        val img_main_weather = findViewById<ImageView>(R.id.img_main_weather)
        var case: String?
        var weather: String? = intent.getStringExtra("weather")
        var temp: String? = intent.getStringExtra("temp")
        val text_main_tempe = findViewById<TextView>(R.id.text_main_temp)
        val text_main_date = findViewById<TextView>(R.id.text_main_date)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val formatted = current.format(formatter)

        text_main_tempe.text = temp + "℃"
        text_main_date.text = formatted

        when(weather) {
            "Clear" -> img_main_weather.setImageResource(R.drawable.sun)
            "Rain" -> img_main_weather.setImageResource(R.drawable.rain)
            "Snow" -> img_main_weather.setImageResource(R.drawable.snow)
            "Drizzle" -> img_main_weather.setImageResource(R.drawable.drizzle)
            "Clouds" -> img_main_weather.setImageResource(R.drawable.cloud)
            "thunderstorm" -> img_main_weather.setImageResource(R.drawable.thunderstorm)
            else -> img_main_weather.setImageResource(R.drawable.sun)
        }

        if(intent.hasExtra("case")) {
            case = intent.getStringExtra("case")
        } else {
            case = "case1"
        }

        btn_main_cage1.setOnClickListener {
            case = "case1"
        }

        btn_main_cage2.setOnClickListener {
            case = "case2"
        }

        btn_main_temp.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "온도")
            intent.putExtra("case" , case)
            startActivity(intent)
        }

        btn_main_light.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "조도")
            intent.putExtra("case" , case)
            startActivity(intent)
        }

        btn_main_moisture.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "수분")
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