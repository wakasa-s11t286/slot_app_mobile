package com.example.slot_analysis_v2

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import java.math.RoundingMode
import java.text.DecimalFormat


class SubActivity3 : AppCompatActivity() {
    private lateinit var helper: DBOpenHelper
    private lateinit var db: SQLiteDatabase

    val df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub3)

        df.roundingMode = RoundingMode.DOWN

        //DB（DBが存在しない場合は新規にファイルに作成）
        helper = DBOpenHelper(applicationContext)
        db = helper.writableDatabase

        // xmlにて実装したListViewの取得
        val listView = findViewById<ListView>(R.id.list_view2)

        //DBからデータ取得
        val arrayList = getListRecord()

        // ArrayAdapterの生成
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)

        // ListViewに、生成したAdapterを設定
        listView.adapter = adapter


    }

    //DBから終了済みのデータを取得
    private fun getListRecord(): ArrayList<String> {
        val arrayList = arrayListOf<String>()

        //DBデータ取得
        val cursor = db.query(
            "result", arrayOf("id","date", "start","end","total","koyaku1","koyaku2","bb","rb","cherrybb","cherryrb"),
            "end is not null",
            null,
            null,
            null,
            null
        )
        if(cursor.count == 0){
            arrayList.add("データなし")
            return arrayList
        }

        cursor.moveToFirst()

        for (i in 0 until cursor.count) {
            //画面表示ように組み立て
            val sbuilder = StringBuilder()
            sbuilder.append(cursor.getString(0))
            sbuilder.append("_  日付 ：")
            sbuilder.append(cursor.getString(1))
            sbuilder.append(" _ ブドウ：")
            val temp1:Double = cursor.getInt(4).toDouble() / cursor.getInt(5)
            sbuilder.append( df.format(temp1))

            sbuilder.append(" _ BB：")
            val temp2:Double = cursor.getInt(4).toDouble() / (cursor.getInt(7) + cursor.getInt(9))
            sbuilder.append( df.format(temp2))

            sbuilder.append(" _ RB：")
            val temp3:Double = cursor.getInt(4).toDouble() / (cursor.getInt(8) + cursor.getInt(10))
            sbuilder.append( df.format(temp3))

            sbuilder.append(" _ 合算：")
            val temp4:Double = cursor.getInt(4).toDouble() / (cursor.getInt(7) +cursor.getInt(8) + cursor.getInt(9) + cursor.getInt(10))
            sbuilder.append( df.format(temp4))

            arrayList.add(sbuilder.toString())
            cursor.moveToNext()
        }

        // 忘れずに！
        cursor.close()
        return arrayList
    }

}