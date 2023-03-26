package com.example.taulaperiodica

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.io.*

class KahootActivity : AppCompatActivity() {

    companion object{
        val EXTRA_ELEMENTS: String = "elements"
    }

    private lateinit var elements: List<Element>
    private lateinit var elementLayout: ElementLayout
    private lateinit var tvTitle: TextView
    private lateinit var originalextTitle: String
    private lateinit var tvScore: TextView
    private lateinit var tvHighestScore: TextView
    private lateinit var btnSolid: Button
    private lateinit var btnGas: Button
    private lateinit var btnLiquid: Button
    private lateinit var btnUnknown: Button

    private var score: Int = 0
    private var highestScore: Int = 0

    private lateinit var currentElement: Element

    private val fileName: String = "dataScore"

    private var play: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kahoot)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        elements = intent.extras?.getSerializable(EXTRA_ELEMENTS) as List<Element>

        elementLayout = findViewById(R.id.elementViewOnce)
        tvTitle = findViewById(R.id.tvTitleKahoot)
        originalextTitle = tvTitle.text.toString()
        tvScore = findViewById(R.id.tvScore)
        tvHighestScore = findViewById(R.id.tvHighestScore)

        btnSolid = findViewById(R.id.btnSolid)
        btnGas = findViewById(R.id.btnGas)
        btnLiquid = findViewById(R.id.btnLiquid)
        btnUnknown = findViewById(R.id.btnUnknown)

        loadButtons(btnSolid, btnGas, btnLiquid, btnUnknown)

        if(read() == -1){
            SnackbarCustom.showSnackbarError(this, getString(R.string.error_read))
            highestScore = 0
        }
        updateScore()

        random()

    }

    fun loadButtons(vararg btn: Button){

        val data = Data(this)

        btn.forEach {
            it.setBackgroundColor(data.phase_data[it.text.toString()]?.color ?: data.phase_data["Unknown"]!!.color)
            it.setOnClickListener{ btn ->

                play = true

                if (correctOption(btn as Button)){
                    score++
                    updateScore()
                    random()
                }else lost()

            }
        }

    }

    private fun updateScore(){
        tvScore.text = score.toString()

        if(score > highestScore) highestScore = score

        tvHighestScore.text = highestScore.toString()

    }

    private fun correctOption(btn: Button) = currentElement.phase == btn.text.toString()

    private fun lost(){
        arrayOf(btnGas, btnSolid, btnLiquid, btnUnknown).forEach { it.setBackgroundColor(getColor(if(correctOption(it)) R.color.correct_select else R.color.wrong_select)) }

        play = false

        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.title_lost))
            .setMessage(getString(R.string.message_lost).format(currentElement.name, currentElement.phase, score, highestScore))
            .setPositiveButton(getString(R.string.continue_button)){_, _ ->
                if(!save()) SnackbarCustom.showSnackbarError(this, getString(R.string.error_save))
                finish()
            }
            .show()
    }

    private fun save(scoreSave: Int = highestScore): Boolean{

        val fileOutputStream: FileOutputStream

        try{

            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)


            fileOutputStream.write(scoreSave.toString().toByteArray())

        }catch (e: Exception){
            e.printStackTrace()
            return false
        }

        return true
    }

    private fun read(): Int{

        if(!baseContext.getFileStreamPath(fileName).exists()) return 0

        val fileInputStream: InputStream

        try{

            fileInputStream = openFileInput(fileName)

            val stream = InputStreamReader(fileInputStream)

            val buffer = BufferedReader(stream)

            highestScore = buffer.readLine().toInt()

        }catch (e: Exception){
            e.printStackTrace()
            return -1
        }

        return highestScore

    }

    private fun loadElement(element: Element){

        elementLayout.buildElement(element)

        tvTitle.text = originalextTitle.format(element.name)

    }

    fun random(){

        var element: Element

        do element = elements.random() while (this::currentElement.isInitialized && element.number == currentElement.number)

        currentElement = element

        loadElement(currentElement)

    }

    override fun finish() {

        if(play) AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.exit))
            .setMessage(getString(R.string.exit_question))
            .setPositiveButton(getString(R.string.exit)){_, _ ->
                save()
                super.finish()
            }
            .setNeutralButton(getString(R.string.cancel_button)){_, _ ->}
            .show()
        else super.finish()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {   // no es el R normal, es el R de sistema
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}