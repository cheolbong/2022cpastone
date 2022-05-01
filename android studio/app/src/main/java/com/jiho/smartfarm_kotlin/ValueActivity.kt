package com.jiho.smartfarm_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ValueActivity : AppCompatActivity() {

    private var Adapter: RecycleAdapter? = null
    private var Review: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value)

        val btn_value_home = findViewById<ImageButton>(R.id.btn_value_home)
        val text_value_value = findViewById<TextView>(R.id.text_value_value)
        val btn_value_cage1 = findViewById<Button>(R.id.btn_value_cage1)
        val btn_value_cage2 = findViewById<Button>(R.id.btn_value_cage2)
        val btn_value_setting = findViewById<Button>(R.id.btn_value_setting)
        var cage:String? = intent.getStringExtra("cage")
        val text_value_cagenumber = findViewById<TextView>(R.id.text_value_cagenumber)

        text_value_cagenumber.text = cage

        text_value_value.text = intent.getStringExtra("value")

        btn_value_cage1.setOnClickListener {
            cage = "cage1"
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_value_cage2.setOnClickListener {
            cage = "cage2"
            val intent = Intent(this, ValueActivity::class.java)
            intent.putExtra("cage", cage)
            startActivity(intent)
            finish()
        }

        btn_value_home.setOnClickListener {
            intent.putExtra("cage", cage)
            finish()
        }

        btn_value_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        val list:MutableList<RecycleData> = mutableListOf()

        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))
        list.add(RecycleData("2022-04-07", "24도"))

        Review = findViewById(R.id.reView)
        Review!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Adapter = RecycleAdapter(list)
        Review!!.adapter = Adapter
    }
}