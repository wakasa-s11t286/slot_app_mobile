package com.example.slot_analysis_v2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import org.json.JSONObject


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

        //DBからデータを取得
        val resultchart = getDetailRecord(param)

        if (resultchart != null) {
            getMyAPI(resultchart)
        }


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

    //APIコール
    private fun getMyAPI(list:ArrayList<String>) {

        //配列を文字列に変換
        var chartTxt = list.toString()
        chartTxt = chartTxt.drop(1)
        chartTxt = chartTxt.dropLast(1)
        chartTxt = chartTxt.replace("\\s".toRegex(), "")

        var resultList = ArrayList<String>()

        Log.i("test",chartTxt)
        /// リクエストURL
        val url = "https://wakabapg.pythonanywhere.com/getchart/$chartTxt"

        //val url = "http://127.0.0.1:5000/getchart/$chartTxt/"
        /// ヘッダー（キー・値のHashMap）

        Log.i("test1",url)
        val headers = hashMapOf(
            "Content-Type" to "application/json"
        )

        /// GETリクエスト送信！

        Fuel.get(url).header(headers).responseJson {
                request, response, result ->

            when (result) {
                is Result.Failure -> {
                    /// リクエスト失敗・エラー
                    val ex = result.getException()
                    Log.i("error", "Failure : $ex")
                }
                is Result.Success -> {
                    /// レスポンス正常取得
                    /// JSONObjectに変換
                    val data = result.get().obj()
                    Log.i("API結果", "Responsed JSON : "
                            +data.toString())
                    var tmp  = data.getJSONArray("result")

                    for (i in 0 until tmp.length()) {
                        resultList.add(tmp.getString(i))
                    }
                    //グラフ作成
                    createChart(resultList, chartTxt)
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

    //引数のデータをもとにグラフを作成
    private fun createChart(charts:ArrayList<String>, zisseki:String){
        val lineChart = findViewById<LineChart>(R.id.linechart)

        //①Entryにデータ格納
        var entryList1 = mutableListOf<Entry>()//1本目の線
        var entryList2 = mutableListOf<Entry>()//2本目の線
        var entryList3= mutableListOf<Entry>()//3本目の線

        var entryList0 = mutableListOf<Entry>()//実績

        //実績の設定
        val myZissekiList:List<String>  = zisseki.split(",")
        var zissekiCharts = ArrayList(myZissekiList)
        for(i in zissekiCharts.indices) {
            entryList0.add(
                Entry(i.toFloat(), zissekiCharts[i].toInt().toFloat())
            )

        }

        val entries = arrayOf(entryList1,entryList2,entryList3)

        for(i in charts.indices){
            val myList:List<String>  = charts[i].split(",")
            var chart = ArrayList(myList)

            for(j in chart.indices) {
                entries[i].add(
                        Entry(j.toFloat(), chart[j].toInt().toFloat())
                )
            }


        }

        //LineDataSetのList
        val lineDataSets = mutableListOf<ILineDataSet>()
        //実績グラフ
        val lineDataSet0 = LineDataSet(entryList0, "実績")
        lineDataSet0.color = Color.RED
        //近似グラフ1
        val lineDataSet1 = LineDataSet(entryList1, "予測1")
        lineDataSet1.color = Color.BLUE
        //近似グラフ2
        val lineDataSet2 = LineDataSet(entryList2, "予測2")
        lineDataSet2.color = Color.GREEN
        //近似グラフ3
        val lineDataSet3 = LineDataSet(entryList3, "予測3")
        lineDataSet3.color = Color.MAGENTA
        //リストに格納
        lineDataSets.add(lineDataSet0)
        lineDataSets.add(lineDataSet1)
        lineDataSets.add(lineDataSet2)
        lineDataSets.add(lineDataSet3)

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
    }


}