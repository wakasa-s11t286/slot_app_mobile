package com.example.slot_analysis_v2

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class SubActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub5)

        //前画面からパラメータを受け取る
        val intent = intent
        val param: String? = intent.getStringExtra("DEVICE")

        // ProgressBarの取得
        val progressBar = findViewById<ProgressBar>(R.id.progress)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        val url:String = "https://wakabapg.pythonanywhere.com$param"
        //webView.loadUrl(url)

        webView!!.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, weburl: String) {
                super.onPageFinished(view, url)
                // ProgressBarの非表示
                progressBar.visibility = View.GONE
            }
        }

        webView!!.loadUrl(url)

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