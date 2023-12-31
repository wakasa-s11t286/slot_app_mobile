package com.example.slot_analysis_v2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import java.util.Objects
import kotlin.math.ceil
import kotlin.math.roundToInt
import android.view.WindowManager


class SubActivity1 : AppCompatActivity() {

    private lateinit var helper: DBOpenHelper
    private lateinit var db: SQLiteDatabase

    //デバック用(30秒)
    private val timeCount :Long = 630000
    //private val timeCount :Long = 30000

    private val timer = Timer("BasicTimer")

    private lateinit var countdownTimer: CountDownTimer //ここを追加

    private var param: Int = 0

    //トータルG
    private var count0: Int = 0
    //ブドウ
    private var countA: Int = 0
    //チェリー
    private var countB: Int = 0
    //単独BB
    private var countC: Int = 0
    //単独RB
    private var countD: Int = 0
    //チェリーBB
    private var countE: Int = 0
    //チェリーRB
    private var countF: Int = 0

    val df = DecimalFormat("#.##")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub1)

        //アプリ起動中はスリープ不可
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //DB（DBが存在しない場合は新規にファイルに作成）
        helper = DBOpenHelper(applicationContext)
        db = helper.writableDatabase

        df.roundingMode = RoundingMode.DOWN

        //前画面（続きからor新規）で選択されたIDパラメータを受け取る
        val intent = intent
         param = intent.getIntExtra("PARAMETER",0)

        //DBの値で復元する
        initialCount(param)

        //トータルG
        val button01 = findViewById<Button>(R.id.button_0_1)
        val button02 = findViewById<Button>(R.id.button_0_2)
        val button03 = findViewById<Button>(R.id.button_0_3)
        val editTextNumber0 = findViewById<EditText>(R.id.editTextNumber0) // TextViewを取得

        //ブドウ
        val buttonA = findViewById<Button>(R.id.button_A) // Buttonを取得
        val editTextNumberA = findViewById<EditText>(R.id.editTextNumberA) // TextViewを取得
        val textViewA = findViewById<TextView>(R.id.textViewA)

        //チェリー
        val buttonB = findViewById<Button>(R.id.button_B) // Buttonを取得
        val editTextNumberB = findViewById<EditText>(R.id.editTextNumberB) // TextViewを取得
        val textViewB = findViewById<TextView>(R.id.textViewB)

        //単独BB
        val buttonC = findViewById<Button>(R.id.button_C) // Buttonを取得
        val editTextNumberC = findViewById<EditText>(R.id.editTextNumberC) // TextViewを取得
        val textViewC = findViewById<TextView>(R.id.textViewC)

        //単独RB
        val buttonD = findViewById<Button>(R.id.button_D) // Buttonを取得
        val editTextNumberD = findViewById<EditText>(R.id.editTextNumberD) // TextViewを取得
        val textViewD = findViewById<TextView>(R.id.textViewD)

        //チェリーBB
        val buttonE = findViewById<Button>(R.id.button_E) // Buttonを取得
        val editTextNumberE = findViewById<EditText>(R.id.editTextNumberE) // TextViewを取得
        val textViewE = findViewById<TextView>(R.id.textViewE)

        //チェリーRB
        val buttonF = findViewById<Button>(R.id.button_F) // Buttonを取得
        val editTextNumberF = findViewById<EditText>(R.id.editTextNumberF) // TextViewを取得
        val textViewF = findViewById<TextView>(R.id.textViewF)

        //終了ボタン
        val button2 = findViewById<Button>(R.id.button_2) // Buttonを取得
        //グラフボタン
        val button3 = findViewById<Button>(R.id.button_3) // Buttonを取得

        //タイマーテキスト
        val textTimer = findViewById<TextView>(R.id.timer)

        val textView1 = findViewById<TextView>(R.id.textView1)
        val textView2 = findViewById<TextView>(R.id.textView2)
        val textView3 = findViewById<TextView>(R.id.textView3)
        val textView4 = findViewById<TextView>(R.id.textView4)


        //初期化
        (editTextNumber0 as TextView).text = count0.toString()
        (editTextNumberA as TextView).text = countA.toString()
        (editTextNumberB as TextView).text = countB.toString()
        (editTextNumberC as TextView).text = countC.toString()
        (editTextNumberD as TextView).text = countD.toString()
        (editTextNumberE as TextView).text = countE.toString()
        (editTextNumberF as TextView).text = countF.toString()

        //トータルGの加算
        button01.setOnClickListener {
            count0 += 10
            (editTextNumber0 as TextView).text = count0.toString()

        }
        button02.setOnClickListener {
            count0 += 50
            (editTextNumber0 as TextView).text = count0.toString()

        }
        button03.setOnClickListener {
            count0 += 100
            (editTextNumber0 as TextView).text = count0.toString()

        }

        //ブドウボタン押下時
        buttonA.setOnClickListener {
            countA += 1
            (editTextNumberA as TextView).text = countA.toString()

        }
        //チェリーボタン押下時
        buttonB.setOnClickListener {
            countB += 1
            (editTextNumberB as TextView).text = countB.toString()

        }

        //単独BBボタン押下時
        buttonC.setOnClickListener {
            countC += 1
            (editTextNumberC as TextView).text = countC.toString()

        }

        //単独RBボタン押下時
        buttonD.setOnClickListener {
            countD += 1
            (editTextNumberD as TextView).text = countD.toString()

        }

        //チェリーBBボタン押下時
        buttonE.setOnClickListener {
            countE += 1
            (editTextNumberE as TextView).text = countE.toString()

        }

        //チェリーRBボタン押下時
        buttonF.setOnClickListener {
            countF += 1
            (editTextNumberF as TextView).text = countF.toString()

        }

        //テキストラベルの変更時の処理(トータルG)
        editTextNumber0.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var textA = "0"
                var textB = "0"
                var textC = "0"
                var textD = "0"
                var textE = "0"
                var textF = "0"
                //処理を色々書く
                if (p0.toString() != ""){
                    count0 = p0.toString().toInt()

                    //ブドウ確率
                    val tempRateA : Double = count0 / editTextNumberA.text.toString().toDouble()
                    var settingA = getSettingA(tempRateA)
                    textA = "1/"+df.format(tempRateA)+"($settingA)"

                    //チェリー確率
                    val tempRateB : Double = count0 / editTextNumberB.text.toString().toDouble()
                    var settingB = getSettingB(tempRateB)
                    textB = "1/"+df.format(tempRateB)+"($settingB)"

                    //単独BB確率
                    val tempRateC : Double = count0 / editTextNumberC.text.toString().toDouble()
                    var settingC = getSettingC(tempRateC)
                    textC = "1/"+df.format(tempRateC)+"($settingC)"

                    //単独RB確率
                    val tempRateD : Double = count0 / editTextNumberD.text.toString().toDouble()
                    var settingD = getSettingD(tempRateD)
                    textD = "1/"+df.format(tempRateD)+"($settingD)"

                    //チェリーBB確率
                    val tempRateE : Double = count0 / editTextNumberE.text.toString().toDouble()
                    var settingE = getSettingE(tempRateE)
                    textE = "1/"+df.format(tempRateE)+"($settingE)"

                    //チェリーRB確率
                    val tempRateF : Double = count0 / editTextNumberF.text.toString().toDouble()
                    var settingF = getSettingF(tempRateF)
                    textF = "1/"+df.format(tempRateF)+"($settingF)"

                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    count0 = 0
                }

                textViewA.text = textA
                textViewB.text = textB
                textViewC.text = textC
                textViewD.text = textD
                textViewE.text = textE
                textViewF.text = textF

                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(ブドウ)
        editTextNumberA.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                        countA = p0.toString().toInt()
                        val tempRate : Double = count0 / p0.toString().toDouble()
                        setting = getSettingA(tempRate)
                        text = "1/"+df.format(tempRate)+"($setting)"
                        calcCommonRate(textView1,textView2,textView3,textView4)
                } else {
                    countA = 0
                    text = "0"
                }
                textViewA.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(チェリー)
        editTextNumberB.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                    countB = p0.toString().toInt()
                    val tempRate : Double = count0 / p0.toString().toDouble()
                    setting = getSettingB(tempRate)
                    text = "1/"+df.format(tempRate)+"($setting)"
                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    countB = 0
                    text = "0"
                }
                textViewB.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(単独BB)
        editTextNumberC.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                    countC = p0.toString().toInt()
                    val tempRate : Double = count0 / p0.toString().toDouble()
                    setting = getSettingC(tempRate)
                    text = "1/"+df.format(tempRate)+"($setting)"
                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    countC = 0
                    text = "0"
                }
                textViewC.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(単独RB)
        editTextNumberD.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                    countD = p0.toString().toInt()
                    val tempRate : Double = count0 / p0.toString().toDouble()
                    setting = getSettingD(tempRate)
                    text = "1/"+df.format(tempRate)+"($setting)"
                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    countD = 0
                    text = "0"
                }
                textViewD.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(チェリーBB)
        editTextNumberE.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                    countE = p0.toString().toInt()
                    val tempRate : Double = count0 / p0.toString().toDouble()
                    setting = getSettingE(tempRate)
                    text = "1/"+df.format(tempRate)+"($setting)"
                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    countE = 0
                    text = "0"
                }
                textViewE.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //テキストラベルの変更時の処理(チェリーRB)
        editTextNumberF.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var text = ""
                var setting = "0"

                if (p0.toString() != ""){
                    countF = p0.toString().toInt()
                    val tempRate : Double = count0 / p0.toString().toDouble()
                    setting = getSettingF(tempRate)
                    text = "1/"+df.format(tempRate)+"($setting)"
                    calcCommonRate(textView1,textView2,textView3,textView4)

                } else {
                    countF = 0
                    text = "0"
                }
                textViewF.text = text
                //DB更新
                updateRecord(param)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //未使用　
            }
        })

        //終了ボタン押下時の処理
        button2.setOnClickListener {
            //もし途中のデータがある場合ダイアログ表示
            AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("実践終了しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    //OKの場合
                    //DBの終了時間を設定
                    updateRecordFinish(param)

                    //タイマー破棄
                    timer.cancel()

                    //実践結果をサーバへ登録
                    updateDataForServer(param)

                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Cancelの時は何もしない
                }
                .show()

        }

        //グラフボタン押下時の処理
        button3.setOnClickListener {
            // インテントの作成
            val intent = Intent(this, SubActivity4::class.java)

            //タイマー破棄
            timer.cancel()

            //次画面に渡すパラメータを設定
            intent.putExtra("PARAMETER", param);

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }

        }


        //カウントダウンタイマー
        countdownTimer = object : CountDownTimer(timeCount, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 1秒ごとにテキストを更新
                val second = ceil(millisUntilFinished / 1000.0).toInt()
                val min = second / 60;
                val sec = second % 60;
                textTimer.text = min.toString() +"：" + sec.toString()
            }

            override fun onFinish() {
                // 各Viewの終了設定
                textTimer.text = "00:00"

            }
        }

        //初期タイマー起動
        //画面に初期値セット
        textTimer.text = timeToText(timeCount)
        //タイマー作動
        countdownTimer.start()


        //定期実行（10.5分おきに詳細(差枚）を記録）
        setTimer(param)

    }

    //アプリが見えなくなった時のイベント
    override fun onStop() {
        super.onStop()
        //タイマー破棄
        timer.cancel()

    }

    //アプリがSTOPから復帰した時のイベント
    override fun onRestart() {
        super.onRestart()
        setTimer(param)

    }

    //戻るボタン押下時
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage("トップへ戻りますか？（データは保存されています）")
            .setPositiveButton("OK") { dialog, which ->
                //OKの場合
                // インテントの作成
                val intent = Intent(this, MainActivity::class.java)
                //タイマー破棄
                timer.cancel()
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Cancelの時は何もしない
            }
            .show()

    }


    private fun setTimer(param: Int){

        //定期実行（10.5分おきに詳細(差枚）を記録）
        timer.scheduleAtFixedRate(timeCount, timeCount) {

            //タイマーテキスト
            val textTimer = findViewById<TextView>(R.id.timer)

            Log.i("Timer","更新起動")
            //タイマーストップ
            countdownTimer.cancel()
            //画面に初期値セット
            textTimer.text = timeToText(timeCount)
            //タイマー作動
            countdownTimer.start()

            updateDetail(param)

            setChartDataDisplay(param)

        }
    }
    private fun timeToText(time:Long): String {
        val second = ceil(time / 1000.0).toInt()
        val min = second / 60;
        val sec = second % 60;
        return "$min：$sec"
    }

    fun getSettingA(rate :Double): String {
        var setting = if(rate <= 5.67){
            "6"
        } else if (rate > 5.67 && rate <= 5.76) {
            "5"
        } else if (rate > 5.76 && rate <= 5.84) {
            "2~4"
        }else {
            "1"
        }
        return setting
    }

    fun getSettingB(rate :Double): String {
        var setting = if(rate <= 35.62){
            "4~6"
        } else if (rate > 35.62 && rate <= 36.82) {
            "3"
        } else {
            "1~2"
        }
        return setting
    }

    fun getSettingC(rate :Double): String {
        var setting = if(rate <= 334.89){
            "6"
        } else if (rate > 334.89 && rate <= 346.50) {
            "5"
        } else if (rate > 346.50 && rate <= 382.26) {
            "4"
        }else if (rate > 382.26 && rate <= 394.01) {
            "3"
        }else if (rate > 394.01 && rate <= 400.00) {
            "2"
        }else {
            "1"
        }
        return setting
    }

    fun getSettingD(rate :Double): String {
        var setting = if(rate <= 330.59){
            "6"
        } else if (rate > 330.59 && rate <= 381.67) {
            "5"
        } else if (rate > 381.67 && rate <= 402.25) {
            "4"
        }else if (rate > 402.25 && rate <= 498.00) {
            "3"
        }else if (rate > 498.00 && rate <= 598.80) {
            "2"
        }else {
            "1"
        }
        return setting
    }

    fun getSettingE(rate :Double): String {
        var setting = if(rate <= 1149.42){
            "4"
        } else if (rate > 1149.42 && rate <= 1199.04) {
            "6"
        } else if (rate > 1199.04 && rate <= 1240.69) {
            "5"
        }else if (rate > 1149.42 && rate <= 1285.34) {
            "3"
        }else if (rate > 1285.34 && rate <= 1388.88) {
            "2"
        }else {
            "1"
        }
        return setting
    }

    fun getSettingF(rate :Double): String {
        var setting = if(rate <= 814.33){
            "4"
        } else if (rate > 814.33 && rate <= 871.08) {
            "6"
        } else if (rate > 871.08 && rate <= 1057.08) {
            "5"
        }else if (rate > 1057.08 && rate <= 1075.26) {
            "3"
        }else if (rate > 1075.26 && rate <= 1173.70) {
            "2"
        }else {
            "1"
        }
        return setting
    }

    @SuppressLint("SetTextI18n")
    fun calcCommonRate(totalSetting:TextView, big:TextView, reg:TextView, addup:TextView) {
        df.roundingMode = RoundingMode.DOWN
        //if(count0==0 || countA==0 || countC==0 || countD==0 || countE==0 || countF==0){
        if(count0==0 ){
            return
        }
        //BIG合成
        val bigRate:Double =  count0.toDouble() / (countC + countE)
        //設定
        val bigSetting = if(bigRate <= 229.1){
            "6"
        } else if(bigRate > 229.1 && bigRate <= 240.1){
            "5"
        } else if(bigRate > 240.1 && bigRate <= 254.0){
            "4"
        } else if(bigRate > 254.0 && bigRate <= 266.4){
            "3"
        } else if(bigRate > 266.4 && bigRate <= 2770.8){
            "2"
        } else{
            "1"
        }
        big.text = "1/"+df.format(bigRate)+"($bigSetting)"

        //REG合成
        val regRate:Double = count0.toDouble() / (countD + countF)
        //設定
        val regSetting = if(regRate <= 229.1){
            "6"
        } else if(regRate > 229.1 && regRate <= 268.6){
            "5"
        } else if(regRate > 268.6 && regRate <= 290.0){
            "4"
        } else if(regRate > 290.0 && regRate <= 336.1){
            "3"
        } else if(regRate > 336.1 && regRate <= 385.5){
            "2"
        } else{
            "1"
        }
        reg.text = "1/"+df.format(regRate)+"($regSetting)"

        //合算
        val addupRate:Double = count0.toDouble() / (countC + countD + countE + countF)
        //設定
        val addupSetting = if(addupRate <= 114.6){
            "6"
        } else if(addupRate > 114.6 && addupRate <= 126.8){
            "5"
        } else if(addupRate > 126.8 && addupRate <= 135.4){
            "4"
        } else if(addupRate > 135.4 && addupRate <= 148.6){
            "3"
        } else if(addupRate > 148.6 && addupRate <= 159.1){
            "2"
        } else{
            "1"
        }
        addup.text = "1/"+df.format(addupRate)+"($addupSetting)"

        //トータル設定(１．合算、２．ブドウ。３．単レグ)
        //単レグ
        val commonReg:Double = count0.toDouble() / countD
        val commonRegSetting = getSettingD(commonReg)

        //ブドウ
        val budou:Double = count0.toDouble() / countA
        val budouSetting = getSettingA(budou)

        val total = if(budouSetting != "2~4"){
            (addupSetting.toInt() + commonRegSetting.toInt() + budouSetting.toInt()) / 3
        } else {
            (addupSetting.toInt() + commonRegSetting.toInt()) / 2
        }
        totalSetting.text = total.toString()
    }

    private fun updateRecord(id:Int){
        val values = ContentValues()
        values.put("total", count0)
        values.put("koyaku1", countA)
        values.put("koyaku2", countB)
        values.put("bb", countC)
        values.put("rb", countD)
        values.put("cherrybb", countE)
        values.put("cherryrb", countF)

        db.update("result",values,"id=$id",null)
    }

    private fun updateRecordFinish(id:Int){
        //現在時間を取得
        val formatter = SimpleDateFormat("hh:mm")
        val currentTime = formatter.format(Date())
        val end: String = currentTime

        val values = ContentValues()
        values.put("end", end)

        db.update("result",values,"id=$id",null)


    }

    private fun updateDetail(id:Int){
        val formatter = SimpleDateFormat("hh:mm")
        val currentTime = formatter.format(Date())
        val time: String = currentTime

        val charts = getDetailRecord(id)

        //現在の差枚を算出
        //推測でリプレイ、ピエロ、ベルの値を算出
        //リプレイ回数(1/7.3)
        val replay  = (count0 / 7.3).roundToInt()
        //ピエロ回数(1/ 1024.0)
        val piero  = (count0 / 1024.0).roundToInt()
        //ベル回数(1/ 1024.0)
        val bell  = (count0 / 1024.0).roundToInt()


        //投資枚数
        val investment = (count0-replay) * 3
        //回収枚数
        val collect1 = countA * 8
        val collect2 = countB * 1
        val collect3 = (countC + countE) * 240
        val collect4 = (countD + countF) * 96
        val collect5 = piero * 10
        val collect6 = bell * 14
        //差枚
        val output = (collect1 + collect2 +collect3 + collect4 + collect5 + collect6) - investment

        if (charts != null) {
            if(charts.last() == output.toString()){
                return
            }

            charts.add(output.toString())

            var regist = charts.toString()
            regist = regist.drop(1)
            regist = regist.dropLast(1)
            regist = regist.replace("\\s".toRegex(), "")

            val values = ContentValues()
            values.put("time", time)
            values.put("chart", regist)

            Log.i("更新結果",regist)
            db.update("detail",values,"resultid=$id",null)
        }

    }

    private fun setChartDataDisplay(id:Int){

        val chartdataView = findViewById<TextView>(R.id.output)

        val charts = getDetailRecord(id)

        if (charts != null) {

            var regist = charts.toString()
            regist = regist.drop(1)
            regist = regist.dropLast(1)
            regist = regist.replace("\\s".toRegex(), "")

            chartdataView.text = regist

        }

    }

    //DBから取得した内容でカウンターを初期化する
    private fun initialCount(id:Int){
        //DBデータをID指定で取得
        val cursor = db.query(
            "result", arrayOf("id","date", "total","koyaku1","koyaku2","bb","rb","cherrybb","cherryrb"),
            "id=$id",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()

        if(cursor.getInt(2) != null){
            //DBの値がNULLでなければ、各カウンターを更新
            count0 = cursor.getInt(2)
            countA = cursor.getInt(3)
            countB = cursor.getInt(4)
            countC = cursor.getInt(5)
            countD = cursor.getInt(6)
            countE = cursor.getInt(7)
            countF = cursor.getInt(8)
        }

        // 忘れずに！
        cursor.close()

        setChartDataDisplay(id)
    }

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

    //APIでサーバへデータを登録
    @SuppressLint("HardwareIds")
    private fun updateDataForServer(id:Int) {

        // ProgressBarの取得
        val progressBar = findViewById<ProgressBar>(R.id.progress3)
        //ローディングスタート
        progressBar.visibility = View.VISIBLE

        //サーバへ送る各パラメーター

        //端末のUIDを取得
        var device :String = Settings.Secure.getString(this.getContentResolver(), Settings.System.ANDROID_ID);

        var total: Int = 0
        var big: Int = 0
        var regular:Int = 0
        var cbig:  Int = 0
        var cregular:  Int = 0
        var koyaku1:  Int = 0
        var koyaku2:  Int = 0
        var chart:  String = ""


        //実践結果テーブル検索
        val cursor = db.query(
            "result", arrayOf("id","date", "total","koyaku1","koyaku2","bb","rb","cherrybb","cherryrb"),
            "id=$id",
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()

        if(cursor.getInt(2) != null){
            //DBの値がNULLでなければ、各カウンターを更新
            total = cursor.getInt(2)
            koyaku1 = cursor.getInt(3)
            koyaku2 = cursor.getInt(4)
            big = cursor.getInt(5)
            regular = cursor.getInt(6)
            cbig = cursor.getInt(7)
            cregular = cursor.getInt(8)
        }
        cursor.close()

        //詳細テーブルを検索
        val cursor2 = db.query(
            "detail", arrayOf("id","resultid", "time", "chart"),
            "resultid=$id",
            null,
            null,
            null,
            null
        )
        cursor2.moveToFirst()

        if(cursor2.getInt(3) != null){
            //DBの値がNULLでなければ、チャート情報を取得
            chart = cursor2.getString(3)
        }
        cursor2.close()

        /// リクエストURL
        val url = "https://wakabapg.pythonanywhere.com/updatedata"

        val headers = hashMapOf(
            "Content-Type" to "application/json"
        )

        //POSTするパラメーターを設定
        val json = JSONObject()
        json.put("device", device)
        json.put("total", total)
        json.put("big", big)
        json.put("regular", regular)
        json.put("cbig", cbig)
        json.put("cregular", cregular)
        json.put("koyaku1", koyaku1)
        json.put("koyaku2", koyaku2)
        json.put("chart", chart)

        /// POSTリクエスト送信！
        Fuel.post(url).header(headers).body(json.toString()).responseJson {
                request, response, result ->

            when (result) {
                is Result.Failure -> {
                    /// リクエスト失敗・エラー
                    val ex = result.getException()
                    Log.i("error", "Failure : $ex")
                    //ローディング了
                    progressBar.visibility = View.GONE

                    //ダイアログ表示
                    AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage("サーバへのデータ送信に失敗しました")
                        .setPositiveButton("OK") { dialog, which ->
                            // OKでTOPへ
                            // インテントの作成
                            val intent = Intent(this, MainActivity::class.java)

                            // TOPへ遷移
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }
                        }
                        .show()
                }
                is Result.Success -> {
                    /// レスポンス正常取得
                    /// JSONObjectに変換
                    val data = result.get().obj()
                    Log.i("API結果", "Responsed JSON : "
                            +data.toString())
                    var deviceparam = data.getString("result")

                    //ローディング了
                    progressBar.visibility = View.GONE

                    //ダイアログ表示
                    AlertDialog.Builder(this)
                        .setTitle("警告")
                        .setMessage("終了した実践データを確認しますか？")
                        .setPositiveButton("OK") { dialog, which ->
                            //OKの場合
                            // インテントの作成
                            val intent = Intent(this, SubActivity5::class.java)

                            //次画面に渡すパラメータを設定
                            intent.putExtra("DEVICE", deviceparam);

                            // WEBページへ遷移
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }

                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            // Cancelの時はTOPへ
                            // インテントの作成
                            val intent = Intent(this, MainActivity::class.java)

                            // TOPへ遷移
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }
                        }
                        .show()

                }

                else -> {
                    //ローディング了
                    progressBar.visibility = View.GONE
                }
            }

        }
    }


}