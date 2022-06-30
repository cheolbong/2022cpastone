package com.jiho.smartfarm_kotlin

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class CamActivity : AppCompatActivity() {

    private val TAG = "MainActivity::"
    private var stream_thread: HandlerThread? = null
    private  var flash_thread: HandlerThread? = null
    private  var rssi_thread: HandlerThread? = null
    private var stream_handler: Handler? = null
    private  var flash_handler: Handler? = null
    private  var rssi_handler: Handler? = null

    private var monitor: ImageView? = null

    private val ID_CONNECT:Int = 200
    private val ID_FLASH:Int = 201
    private val ID_RSSI:Int = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)

        monitor = findViewById(R.id.view_cam_cam)



        stream_thread = HandlerThread("http")
        stream_thread!!.start()
        stream_handler = HttpHandler(stream_thread!!.looper)

        flash_thread = HandlerThread("http")
        flash_thread!!.start()
        flash_handler = HttpHandler(flash_thread!!.looper)

        rssi_thread = HandlerThread("http")
        rssi_thread!!.start()
        rssi_handler = HttpHandler(rssi_thread!!.looper)

        stream_handler!!.sendEmptyMessage(ID_CONNECT)
        rssi_handler!!.sendEmptyMessage(ID_RSSI)

        val img_cam_cagenumber = findViewById<ImageView>(R.id.img_cam_casenumber)
        val btn_cam_cage1 = findViewById<ImageButton>(R.id.btn_cam_cage1)
        val btn_cam_cage2 = findViewById<ImageButton>(R.id.btn_cam_cage2)
        val btn_cam_info = findViewById<ImageButton>(R.id.btn_cam_info)
        val btn_cam_back = findViewById<ImageButton>(R.id.btn_cam_back)
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

    inner class HttpHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ID_CONNECT -> VideoStream()
                ID_RSSI -> GetRSSI()
                else -> {}
            }
        }
    }

    private fun GetRSSI() {
        rssi_handler!!.sendEmptyMessageDelayed(ID_RSSI, 500)
        val rssi_url:String = "http://172.20.10.5:80/RSSI"
        try {
            val url = URL(rssi_url)
            try {
                val huc = url.openConnection() as HttpURLConnection
                huc.requestMethod = "GET"
//                huc.connectTimeout = 15000
//                huc.readTimeout = 10000
//                huc.doInput = true
//                huc.connect()
                if (huc.responseCode == 200) {
                    val ins:InputStream = huc.inputStream
                    val isr = InputStreamReader(ins)
                    val br = BufferedReader(isr)
                    val data = br.readLine()
                    if (!data.isEmpty()) {
//                        runOnUiThread { rssi_text!!.text = data }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.d("통신실패",e.toString())
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    private fun VideoStream() {
        val stream_url = "http://172.20.10.5:81/stream"
        var bis: BufferedInputStream? = null
        val fos: FileOutputStream? = null
        try {
            val url = URL(stream_url)
            try {
                val huc = url.openConnection() as HttpURLConnection
                huc.requestMethod = "GET"
//                huc.connectTimeout = 10000
//                huc.readTimeout = 15000
//                huc.doInput = true
                // huc.connect()
                if (huc.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("http call","통신성공")
                    //val ins = huc.inputStream
                    val isr = InputStreamReader(huc.inputStream)
                    val br = BufferedReader(isr)
                    var data: String
                    var len: Int
                    var buffer: ByteArray
                    while (br.readLine().also { data = it } != null) {
                        if (data.contains("Content-Type:")) {
                            data = br.readLine()
                            len = data.split(":").toTypedArray()[1].trim { it <= ' ' }.toInt()
                            bis = BufferedInputStream(huc.inputStream)
                            buffer = ByteArray(len)
                            var t = 0
                            while (t < len) {
                                t += bis.read(buffer, t, len - t)
                            }
                            Bytes2ImageFile(
                                buffer,
                                getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/0A.jpg"
                            )
                            val bitmap =
                                BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/0A.jpg")
                            runOnUiThread { monitor!!.setImageBitmap(bitmap) }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("통신실패",e.toString())
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                fos?.close()
                stream_handler!!.sendEmptyMessageDelayed(ID_CONNECT, 3000)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("통신실패",e.toString())
            }
        }
    }

    private fun Bytes2ImageFile(bytes: ByteArray, fileName: String) {
        try {
            val file = File(fileName)
            val fos = FileOutputStream(file)
            fos.write(bytes, 0, bytes.size)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("파일 실패",e.toString())
        }
    }
}