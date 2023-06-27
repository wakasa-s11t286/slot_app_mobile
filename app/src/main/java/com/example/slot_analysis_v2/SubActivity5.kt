package com.example.slot_analysis_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView

class SubActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub5)

        //前画面からパラメータを受け取る
        val intent = intent
        val param: String? = intent.getStringExtra("DEVICE")

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        val url:String = "https://wakabapg.pythonanywhere.com$param"
        webView.loadUrl(url)

        val button = findViewById<Button>(R.id.top)

        button.setOnClickListener {
            // インテントの作成
            val intent = Intent(this, MainActivity::class.java)

            // TOPへ遷移
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }

        }
    }
}