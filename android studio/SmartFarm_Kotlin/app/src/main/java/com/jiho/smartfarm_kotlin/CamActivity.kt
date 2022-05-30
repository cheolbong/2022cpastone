package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class CamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)

        val img_cam_cagenumber = findViewById<ImageView>(R.id.img_cam_casenumber)
        val btn_cam_cage1 = findViewById<ImageButton>(R.id.btn_cam_cage1)
        val btn_cam_cage2 = findViewById<ImageButton>(R.id.btn_cam_cage2)
        val btn_cam_info = findViewById<ImageButton>(R.id.btn_cam_info)
        val btn_cam_back = findViewById<ImageButton>(R.id.btn_cam_back)
        val btn_cam_ai = findViewById<ImageButton>(R.id.btn_cam_ai)
        var case:String? = intent.getStringExtra("case")

        when(case) {
            "case1" -> {
                img_cam_cagenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_cam_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_cam_cage2.setBackgroundResource(R.drawable.case2_button)
            }
            "case2" -> {
                img_cam_cagenumber.setBackgroundResource(R.drawable.cage2_main)
                btn_cam_cage1.setBackgroundResource(R.drawable.case1_button)
                btn_cam_cage2.setBackgroundResource(R.drawable.case2_push_button)
            }
            else -> {
                img_cam_cagenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_cam_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_cam_cage2.setBackgroundResource(R.drawable.case2_button)
            }
        }

        btn_cam_back.setOnClickListener {
            finish()
        }

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