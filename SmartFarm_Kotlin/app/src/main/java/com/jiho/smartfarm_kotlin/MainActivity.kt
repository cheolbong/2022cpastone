package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

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
        var cage:String?

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