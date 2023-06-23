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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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

        //DBからデータを取得
        val resultchart = getDetailRecord(param)




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

        //lineChart.data = data
        //lineChart.animateY(1000)


        //検証2
        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)//X軸データ
        val y1 = x.map{it}//Y軸データ1（X軸の1乗）
        val y2 = x.map{it*it}//Y軸データ2（X軸の2乗）

        //①Entryにデータ格納
        var entryList1 = mutableListOf<Entry>()//1本目の線
        var entryList2 = mutableListOf<Entry>()//2本目の線


        if (resultchart != null) {

            for(i in resultchart.indices){
                entryList1.add(
                    Entry(i.toFloat(), resultchart[i].toInt().toFloat())
                )


            }
        }

       // entryList1.add(Entry(1f,0f))
       // entryList1.add(Entry(2f,-300f))
       // entryList1.add(Entry(3f,-530f))
       // entryList1.add(Entry(4f,-112f))
       // entryList1.add(Entry(5f,345f))
       // entryList1.add(Entry(6f,820f))
      //  entryList1.add(Entry(7f,1310f))

        entryList2.add(Entry(0f,0f))
        entryList2.add(Entry(1f,300f))
        entryList2.add(Entry(2f,530f))
        entryList2.add(Entry(3f,112f))
        entryList2.add(Entry(4f,620f))
        entryList2.add(Entry(5f,730f))
        entryList2.add(Entry(6f,510f))

        //LineDataSetのList
        val lineDataSets = mutableListOf<ILineDataSet>()
        //②線1本目のデータ格納
        val lineDataSet1 = LineDataSet(entryList1, "実績")
        //③線1本目のフォーマット指定
        lineDataSet1.color = Color.BLUE
        //②線2本目のデータ格納
        val lineDataSet2 = LineDataSet(entryList2, "予測1")
        //③線2本目のフォーマット指定
        lineDataSet2.color = Color.RED
        //リストに格納
        lineDataSets.add(lineDataSet1)
        lineDataSets.add(lineDataSet2)

        //④LineDataにLineDataSet格納
        val lineData = LineData(lineDataSets)
        //⑤LineChartにLineData格納
        lineChart.data = lineData
        //⑥Chartのフォーマット指定
        //X軸の設定 → フォーマット指定処理
        lineChart.xAxis.apply {
            isEnabled = true
            textColor = Color.BLACK
        }
        //左Y軸の設定 → フォーマット指定処理
        lineChart.axisLeft.apply {
            isEnabled = true
            textColor = Color.BLACK
        }
        //右Y軸の設定 → フォーマット指定処理
        lineChart.axisRight.apply {
            isEnabled = false
        }
        //⑦linechart更新
        lineChart.invalidate()



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

    private fun getMyAPI(list:ArrayList<String>){

        //配列を文字列に変換
        var chartTxt = list.toString()
        chartTxt = chartTxt.drop(1)
        chartTxt = chartTxt.dropLast(1)
        chartTxt = chartTxt.replace("\\s".toRegex(), "")

        /// リクエストURL
        //val url = "https://api.blockchain.info/stats"
        val url = "https://wakabapg.pythonanywhere.com/hello/test?param=$chartTxt"
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

    //DBから詳細データを取得
    private fun getDetailRecord(param:Int): ArrayList<String>? {

        //DBデータ取得
        val cursor = db.query(
            "detail", arrayOf("id","resultid", "time", "chart"),
            "resultid=$param",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()

        val tempChart = cursor.getString(3) ?: return null

        val myList:List<String>  = tempChart.split(",")
        var arrayList = ArrayList(myList)



        // 忘れずに！
        cursor.close()
        return arrayList
    }


}