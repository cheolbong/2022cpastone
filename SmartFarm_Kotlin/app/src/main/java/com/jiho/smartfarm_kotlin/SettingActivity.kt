package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val btn_setting_home = findViewById<ImageButton>(R.id.btn_setting_home)
        val btn_setting_cage1 = findViewById<Button>(R.id.btn_setting_cage1)
        val btn_setting_cage2 = findViewById<Button>(R.id.btn_setting_cage2)
        val btn_setting_complete = findViewById<Button>(R.id.btn_setting_complete)
        val text_setting_cagenumber = findViewById<TextView>(R.id.text_setting_cagenumber)
        var edit_text_setting_temp = findViewById<EditText>(R.id.edit_text_setting_temp)
        var edit_text_setting_water = findViewById<EditText>(R.id.edit_text_setting_water)
        var cage: String? = intent.getStringExtra("cage")
        var temp_goal = "0"
        var water_goal = "0"

        text_setting_cagenumber.text = cage + " 설정"

        btn_setting_cage1.setOnClickListener {
            cage = "cage1"
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_setting_cage2.setOnClickListener {
            cage = "cage2"
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_setting_home.setOnClickListener {
            intent.putExtra("cage", cage)
            finish()
        }

        btn_setting_complete.setOnClickListener {
            temp_goal = edit_text_setting_temp.text.toString()
            water_goal = edit_text_setting_water.text.toString()
        }
    }
}