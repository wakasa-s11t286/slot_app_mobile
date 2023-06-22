package com.example.slot_analysis_v2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class SubActivity4 : AppCompatActivity() {

    private lateinit var helper: DBOpenHelper
    private lateinit var db: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {

        //DB（DBが存在しない場合は新規にファイルに作成）
        helper = DBOpenHelper(applicationContext)
        db = helper.writableDatabase

        //前画面（続きからor新規）で選択されたIDパラメータを受け取る
        val intent = intent
        val param: Int = intent.getIntExtra("PARAMETER",0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub4)

        val button04 = findViewById<Button>(R.id.button4)

        val lineChart = findViewById<LineChart>(R.id.linechart)

        val entries: ArrayList<Entry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()

        //実績値
        entries.add(Entry(1f,2f))
        colors.add(ColorTemplate.rgb("#2ecc71"))

        entries.add(Entry(2f,4f))
        colors.add(ColorTemplate.rgb("#2ecc71"))

        entries.add(Entry(3f,6f))
        colors.add(ColorTemplate.rgb("#2ecc71"))

        //予測値
        entries.add(Entry(4f,7f))
        colors.add(ColorTemplate.rgb("#e74c3c"))

        entries.add(Entry(5f,5f))
        colors.add(ColorTemplate.rgb("#e74c3c"))

        entries.add(Entry(6f,3f))
        colors.add(ColorTemplate.rgb("#e74c3c"))

        val dataset = LineDataSet(entries, "出玉枚数")

        val data = LineData(dataset)
        //dataset.setColors(*ColorTemplate.MATERIAL_COLORS) //
        //dataset.setColors(ColorTemplate.rgb("#2ecc71"),ColorTemplate.rgb("#2ecc71"),ColorTemplate.rgb("#2ecc71"),
        //    ColorTemplate.rgb("#e74c3c"),ColorTemplate.rgb("#e74c3c"),ColorTemplate.rgb("#e74c3c")) //

        dataset.setColors(colors)

        lineChart.data = data
        lineChart.animateY(1000)



        //戻るボタン押下時
        button04.setOnClickListener{
            // インテントの作成
            val intent = Intent(this, SubActivity1::class.java)

            //次画面に渡すパラメータを設定
            intent.putExtra("PARAMETER", param);

            // TOPへ遷移
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }

        }
    }

    private fun getMyAPI(){
        /// リクエストURL
        //val url = "https://api.blockchain.info/stats"
        val url = "https://wakabapg.pythonanywhere.com/hello/test"
        /// ヘッダー（キー・値のHashMap）
        val headers = hashMapOf(
            "xxx" to "123"
        )

        /// GETリクエスト送信！
        Fuel.get(url).header(headers).responseJson {
                request, response, result ->

            when (result) {
                is Result.Failure -> {
                    /// リクエスト失敗・エラー
                    val ex = result.getException()
                    //Log.d(TAG, "Failure : "+ex.toString())
                }
                is Result.Success -> {
                    /// レスポンス正常取得

                    /// JSONObjectに変換
                    val data = result.get().obj()
                    Log.i("TAG", "Responsed JSON : "
                            +data.toString())
                }

                else -> {}
            }
        }
    }


}