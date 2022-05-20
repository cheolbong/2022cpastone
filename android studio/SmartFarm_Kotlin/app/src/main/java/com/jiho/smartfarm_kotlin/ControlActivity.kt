package com.jiho.smartfarm_kotlin

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val btn_setting_cage1 = findViewById<Button>(R.id.btn_setting_cage1)
        val btn_setting_cage2 = findViewById<Button>(R.id.btn_setting_cage2)
        val btn_setting_complete = findViewById<Button>(R.id.btn_setting_complete)
        val text_setting_cagenumber = findViewById<TextView>(R.id.text_setting_cagenumber)
        val edit_text_setting_temp = findViewById<EditText>(R.id.edit_text_setting_temp)
        val edit_text_setting_water = findViewById<EditText>(R.id.edit_text_setting_water)
        var case: String? = intent.getStringExtra("case")
        val host: String = "192.168.4.64"

        text_setting_cagenumber.text = case + " 설정"

        btn_setting_cage1.setOnClickListener {
            case = "case1"
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_setting_cage2.setOnClickListener {
            case = "case2"
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("case", case)
            startActivity(intent)
            finish()
        }

        btn_setting_complete.setOnClickListener {
            val temp = edit_text_setting_temp.text.toString()
            val water = edit_text_setting_water.text.toString()
            val task = InsertData()
            task.execute("http://$host/update${case}.php", temp, water)
            Toast.makeText(this, "온도 : ${temp}, 수분 : ${water}", Toast.LENGTH_SHORT).show()
        }
    }

    private class InsertData : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String?): String {

            val serverURL: String? = params[0]
            val temp: String? = params[1]
            val water: String? = params[2]

            val postParameters: String = "temp=$temp&water=$water"

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
                var line: String? = null

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }

                bufferedReader.close();

                return sb.toString();

            } catch (e: Exception) {
                return "Error" + e.message
            }

        }

    }
}