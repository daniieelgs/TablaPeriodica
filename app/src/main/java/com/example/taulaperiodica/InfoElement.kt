package com.example.taulaperiodica

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL

class InfoElement : AppCompatActivity() {

    private lateinit var element: Element

    companion object{
        val EXTRA_ELEMENT = "element"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_element)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        element = intent.extras?.getSerializable(EXTRA_ELEMENT) as Element

        val elementLayout: ElementLayout = findViewById(R.id.elementViewOnce)

        elementLayout.setFavoriteListener { element.favorite = elementLayout.favorite }

        with(element) {

            elementLayout.buildElement(number.toInt(), symbol, name, atomic_mass, category, phase)

            elementLayout.numberElement = number.toInt()
            elementLayout.symbolElement = symbol
            elementLayout.nameElement = name
            elementLayout.phaseElement = phase
            elementLayout.favorite = favorite

            setText(boil, R.id.tvBoilElement)
            setText(category, R.id.tvCategoryElement)
            setText(molar_heat, R.id.tvMolarElement)
            setText(period, R.id.tvPeriodElement)
            setText(phase, R.id.tvPhaseElement)
            setText(discovered_by, R.id.tvDiscover)
            setText(named_by, R.id.tvNamed)
            setText(appearance, R.id.tvAppearance)
            setText(melt, R.id.tvMelt)
            setText(electron_configuration, R.id.tvElectronConfig)
            setText(electron_configuration_semantic, R.id.tvElectronConfigSemantic)
            setText(electron_affinity, R.id.tvElectronAffinity)
            setText(electronegativity_pauling, R.id.tvElectronPauling)
            setText(ionization_energies, R.id.tvIonizationEnergies)
            setText(shells, R.id.tvShells)
            setText(summary, R.id.tvSummary)

            setText(SpannableString(source).apply { this.setSpan(UnderlineSpan(), 0, this.length, 0) }, R.id.tvSource)

            findViewById<TextView>(R.id.tvCpkColor).apply {
                if(cpk_hex != null) (background as GradientDrawable).setColor(Color.parseColor("#$cpk_hex"))
                else{
                    background = null
                    text = "-"
                }
            }

            loadImage(image.url)
        }

    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra(EXTRA_ELEMENT, element as java.io.Serializable)
        setResult(RESULT_OK, intent)
        super.finish()
    }

    private fun loadImage(url: String){
        findViewById<ImageView>(R.id.ivElement).setImageDrawable(readImage(url))
    }

    private fun readImage(url: String): Drawable? {

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val input: InputStream = (URL(url).openStream()) as InputStream

        return Drawable.createFromStream(input, "image")
    }

    private fun setText(text: String?, tvId: Int){
        findViewById<TextView>(tvId).text = text ?: "-"
    }

    private fun setText(text: SpannableString, tvId: Int){
        findViewById<TextView>(tvId).text = text
    }
    private fun setText(text: Long?, tvId: Int) = setText(text?.toString(), tvId)
    private fun setText(text: Double?, tvId: Int) = setText(text?.toString(), tvId)
    private fun setText(text: List<Long>?, tvId: Int) = setText(text?.joinToString(" - "), tvId)
    @JvmName("setTextDoubleList")
    private fun setText(text: List<Double>?, tvId: Int) = setText(text?.joinToString(" - "), tvId)


    fun openWebBrowser(view: View?){

        val link: String = (view as TextView).text.toString()

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent);

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