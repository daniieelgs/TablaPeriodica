package com.example.taulaperiodica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var elements: List<Element>

    private var resultStudyActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        elements = data!!.getSerializableExtra("elements") as List<Element>
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        elements = ElementReader.readElement(this, "elements.json")!!
    }

    fun studyClick(view: View?){
        val intent = Intent(this, StudyActivity::class.java)
        intent.putExtra("elements", elements as java.io.Serializable)

        resultStudyActivity.launch(intent)

    }

    fun playClick(view: View?){

        val intent = Intent(this, KahootActivity::class.java)
        intent.putExtra(KahootActivity.EXTRA_ELEMENTS, elements as java.io.Serializable)

        startActivity(intent)

    }


}