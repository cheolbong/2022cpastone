package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class CamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)

        val btn_cam_home = findViewById<ImageButton>(R.id.btn_cam_home)
        val text_cam_cagenumber = findViewById<TextView>(R.id.text_cam_cagenumber)
        val btn_cam_cage1 = findViewById<Button>(R.id.btn_cam_cage1)
        val btn_cam_cage2 = findViewById<Button>(R.id.btn_cam_cage2)
        val btn_cam_setting = findViewById<Button>(R.id.btn_cam_setting)
        var cage:String? = intent.getStringExtra("cage")

        text_cam_cagenumber.text = cage

        btn_cam_cage1.setOnClickListener {
            cage = "cage1"
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_cam_cage2.setOnClickListener {
            cage = "cage2"
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_cam_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_cam_home.setOnClickListener {
            intent.putExtra("cage", cage)
            finish()
        }
    }
}