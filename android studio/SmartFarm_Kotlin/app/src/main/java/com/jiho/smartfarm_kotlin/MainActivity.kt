package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

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
        val btn_main_setting = findViewById<Button>(R.id.btn_main_setting)
        val img_main_weather = findViewById<ImageView>(R.id.img_main_weather)
        var cage: String?
        var weather: String? = intent.getStringExtra("weather")

        when(weather) {
            "Clear" -> img_main_weather.setImageResource(R.drawable.sun)
            "Rain" -> img_main_weather.setImageResource(R.drawable.rain)
            "Snow" -> img_main_weather.setImageResource(R.drawable.snow)
            "Drizzle" -> img_main_weather.setImageResource(R.drawable.drizzle)
            "Cloud" -> img_main_weather.setImageResource(R.drawable.cloud)
            "thunderstorm" -> img_main_weather.setImageResource(R.drawable.thunderstorm)
            else -> img_main_weather.setImageResource(R.drawable.sun)
        }

        if(intent.hasExtra("cage")) {
            cage = intent.getStringExtra("cage")
        } else {
            cage = "cage1"
        }

        btn_main_cage1.setOnClickListener {
            cage = "cage1"
        }

        btn_main_cage2.setOnClickListener {
            cage = "cage2"
        }

        btn_main_temp.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "온도")
            intent.putExtra("cage" , cage)
            startActivity(intent)
        }

        btn_main_light.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "조도")
            intent.putExtra("cage" , cage)
            startActivity(intent)
        }

        btn_main_moisture.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "수분")
            intent.putExtra("cage" , cage)
            startActivity(intent)
        }

        btn_main_humidity.setOnClickListener {
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("value", "습도")
            intent.putExtra("cage", cage)
            startActivity(intent)
        }

        btn_main_cam.setOnClickListener {
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
        }

        btn_main_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
        }
    }
}