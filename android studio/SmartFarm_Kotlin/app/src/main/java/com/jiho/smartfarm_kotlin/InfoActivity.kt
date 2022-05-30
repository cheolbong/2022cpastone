package com.jiho.smartfarm_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val btn_info_back = findViewById<ImageButton>(R.id.btn_info_back)
        var case = intent.getStringExtra("case")

        btn_info_back.setOnClickListener {
            finish()
        }
    }
}