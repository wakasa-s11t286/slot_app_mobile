package com.example.slot_analysis_v2

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AlertDialog
import java.util.Date
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var helper: DBOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClick(v: View?) {
        // インテントの作成
        val intent = Intent(this, SubActivity1::class.java)

        //DB（DBが存在しない場合は新規にファイルに作成）
        helper = DBOpenHelper(applicationContext)
        db = helper.writableDatabase

        //helper.onUpgrade(db,1,2)

        //DBから取得
        val check = checkSavedata()
        if(!check){
            //もし途中のデータがある場合ダイアログ表示
            AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("未終了のデータがありますが、新規スタートしますか？")
                .setPositiveButton("OK") { dialog, which ->
                    //OKの場合
                    nextAction(intent,db)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Cancelの時は何もしない
                }
                .show()

        } else {
            nextAction(intent,db)
        }
    }

    fun onClick2(v: View?) {
        // インテントの作成
        val intent = Intent(this, SubActivity2::class.java)

        // 遷移先画面の起動
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun onClick3(v: View?) {
        // インテントの作成
        val intent = Intent(this, SubActivity3::class.java)

        // 遷移先画面の起動
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private  fun nextAction(intent:Intent,db: SQLiteDatabase ){
        //DB登録項目を設定
        // 現在日時を表示
        val formatter1 = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = formatter1.format(Date())

        val formatter2 = SimpleDateFormat("hh:mm")
        val currentTime = formatter2.format(Date())


        val date: String = currentDate
        val start: String = currentTime

        // DBへ新規レコードを登録
        val result = insertData(db, date, start)
        //次画面に渡すパラメータを設定
        intent.putExtra("PARAMETER", result.toInt());

        // 遷移先画面の起動
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun insertData(db: SQLiteDatabase, date: String, start: String): Long {

        val values = ContentValues()
        values.put("date", date)
        values.put("start", start)
        return db.insert("result", null, values)
    }

    private fun checkSavedata(): Boolean {
        helper = DBOpenHelper(applicationContext)
        db = helper.readableDatabase

        val cursor = db.query(
            "result", arrayOf("date", "start"),
            "end is null",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        var is_check = cursor.count <= 0

        cursor.close()
        return is_check
    }

    private fun readSaveData(): String {
        helper = DBOpenHelper(applicationContext)
        db = helper.readableDatabase

        val cursor = db.query(
            "result", arrayOf("date", "start"),
            "end is null",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val sbuilder = StringBuilder()
        for (i in 0 until cursor.count) {
            sbuilder.append(cursor.getString(0))
            sbuilder.append(": ")
            sbuilder.append(cursor.getString(1))
            sbuilder.append("\n")
            cursor.moveToNext()
        }

        // 忘れずに！
        cursor.close()
        return sbuilder.toString()
    }
}