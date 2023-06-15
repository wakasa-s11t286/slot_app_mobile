package com.example.slot_analysis_v2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClick(v: View?) {
        // ①インテントの作成
        val intent = Intent(this, SubActivity1::class.java)

        // ②遷移先画面の起動
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}