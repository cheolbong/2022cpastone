package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)

        val text_cam_cagenumber = findViewById<TextView>(R.id.text_cam_cagenumber)
        val btn_cam_cage1 = findViewById<Button>(R.id.btn_cam_cage1)
        val btn_cam_cage2 = findViewById<Button>(R.id.btn_cam_cage2)
        val btn_cam_info = findViewById<Button>(R.id.btn_cam_info)
        var case:String? = intent.getStringExtra("case")

        text_cam_cagenumber.text = case

        btn_cam_cage1.setOnClickListener {
            case = "case1"
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_cam_cage2.setOnClickListener {
            case = "case2"
            val intent = Intent(this, CamActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_cam_info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }
    }
}