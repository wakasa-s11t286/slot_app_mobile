package com.example.slot_analysis_v2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView


class SubActivity2 : AppCompatActivity() {

    private lateinit var helper: DBOpenHelper
    private lateinit var db: SQLiteDatabase

    //リスト長押しメニューを作成
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        //削除メニューを追加。
        menu?.add("選択")
        menu?.add("削除")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub2)

        // xmlにて実装したListViewの取得
        val listView = findViewById<ListView>(R.id.list_view)

        //リストに長押しメニューを設定
        registerForContextMenu(listView)


        //DB（DBが存在しない場合は新規にファイルに作成）
        helper = DBOpenHelper(applicationContext)
        db = helper.writableDatabase

        //DBからデータ取得
        val arrayList = getListRecord()

        // ArrayAdapterの生成
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)

        // ListViewに、生成したAdapterを設定
        listView.adapter = adapter
    }

    //長押し後メニュー選択時の処理
    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)

        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val listView = findViewById<ListView>(R.id.list_view)

        if(item.title == "削除"){
            //削除はダイアログ表示
            AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("削除してよろしいでしょうか？")
                .setPositiveButton("OK") { dialog, which ->
                    //OKの場合

                    val list = info.targetView
                    val selectedItem2 = (list as MaterialTextView).text.toString()
                    //val selectedItem = info.id.toString()
                    //Log.i("list1", "Long click : $selectedItem")
                    //Log.i("list2", "Long click : $selectedItem2")

                    //選択された行のIDを取得
                    val lastIdx: Int = selectedItem2.lastIndexOf('_')
                    val selectedId: String =
                        if (lastIdx < 0) selectedItem2 else selectedItem2.substring(0, lastIdx)

                    //DBから削除
                    deleteRecord(selectedId)

                    //削除後のリストを取得
                    val arrayList = getListRecord()
                    // ArrayAdapterの生成
                    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)

                    // ListViewに、生成したAdapterを設定
                    listView.adapter = adapter

                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Cancelの時は何もしない
                }
                .show()

            //ListViewのIDを取得できる。このIDをもとに削除を行う。
            Log.i("a",info.id.toString())
        }
        if(item.title == "選択"){

            //対象のIDを取得
            val list = info.targetView
            val selectedItem = (list as MaterialTextView).text.toString()

            //選択された行のIDを取得
            val lastIdx: Int = selectedItem.lastIndexOf('_')
            val selectedId: String =
                if (lastIdx < 0) selectedItem else selectedItem.substring(0, lastIdx)

            // インテントの作成
            val intent = Intent(this, SubActivity1::class.java)
            //次画面に渡すパラメータを設定
            intent.putExtra("PARAMETER", selectedId.toInt());
            // 遷移先画面の起動
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        return true
    }


    //DBから未終了のデータを取得
    private fun getListRecord(): ArrayList<String> {
        val arrayList = arrayListOf<String>()

        //DBデータ取得
        val cursor = db.query(
            "result", arrayOf("id","date", "start"),
            "end is null",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()

        for (i in 0 until cursor.count) {
            //画面表示ように組み立て
            val sbuilder = StringBuilder()
            sbuilder.append(cursor.getString(0))
            sbuilder.append("_  開始日時  ")
            sbuilder.append(cursor.getString(1))
            sbuilder.append(" : ")
            sbuilder.append(cursor.getString(2))
            arrayList.add(sbuilder.toString())
            cursor.moveToNext()
        }

        // 忘れずに！
        cursor.close()
        return arrayList
    }

    //レコード削除
    private fun deleteRecord(id:String){
        //該当のIDを削除
        db.delete("result", "id=$id", null)
        db.delete("detail", "resultid=$id", null)
    }

}