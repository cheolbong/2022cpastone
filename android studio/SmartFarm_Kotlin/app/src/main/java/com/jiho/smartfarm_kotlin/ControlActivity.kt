package com.jiho.smartfarm_kotlin

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ControlActivity : AppCompatActivity() {

    var case: String? = ""
    val host: String = "172.20.10.3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val btn_control_cage1 = findViewById<ImageButton>(R.id.btn_control_cage1)
        val btn_control_cage2 = findViewById<ImageButton>(R.id.btn_control_cage2)
        val btn_control_info = findViewById<ImageButton>(R.id.btn_control_info)
        val btn_control_complete_temp = findViewById<ImageButton>(R.id.btn_control_complete_temp)
        val btn_control_complete_water = findViewById<ImageButton>(R.id.btn_control_complete_water)
        val img_control_cagenumber = findViewById<ImageView>(R.id.img_control_casenumber)
        val edit_text_control_temp = findViewById<EditText>(R.id.edit_text_control_temp)
        val edit_text_control_water = findViewById<EditText>(R.id.edit_text_control_water)
        val btn_control_back = findViewById<ImageButton>(R.id.btn_control_back)
        case = intent.getStringExtra("case")

        when(case) {
            "case1" -> {
                img_control_cagenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_control_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_control_cage2.setBackgroundResource(R.drawable.case2_button)
            }
            "case2" -> {
                img_control_cagenumber.setBackgroundResource(R.drawable.cage2_main)
                btn_control_cage1.setBackgroundResource(R.drawable.case1_button)
                btn_control_cage2.setBackgroundResource(R.drawable.case2_push_button)
            }
            else -> {
                img_control_cagenumber.setBackgroundResource(R.drawable.cage1_main)
                btn_control_cage1.setBackgroundResource(R.drawable.case1_push_button)
                btn_control_cage2.setBackgroundResource(R.drawable.case2_button)
            }
        }

        btn_control_back.setOnClickListener {
            finish()
        }

        btn_control_cage1.setOnClickListener {
            case = "case1"
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_control_cage2.setOnClickListener {
            case = "case2"
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_control_info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_control_complete_temp.setOnClickListener {
            val value = edit_text_control_temp.text.toString()
            val task = InsertData()
            task.execute("http://$host/update${case}.php", value, "1")
            Toast.makeText(this, "온도 : ${value}", Toast.LENGTH_SHORT).show()
        }

        btn_control_complete_water.setOnClickListener {
            val value = edit_text_control_water.text.toString()
            val task = InsertData()
            task.execute("http://$host/update${case}.php", value, "2")
            Toast.makeText(this, "토양 수분 : ${value}", Toast.LENGTH_SHORT).show()
        }
    }

    private class InsertData : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {

            val serverURL: String? = params[0]
            val value: String? = params[1]
            val state: String? = params[2]

            val postParameters: String = "value=$value&state=$state"

            try {
                val url = URL(serverURL)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()

                val outputStream: OutputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode: Int = httpURLConnection.responseCode

                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                val sb = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }
                bufferedReader.close()
                return sb.toString()
            } catch (e: Exception) {
                return "Error" + e.message
            }
        }
    }
}