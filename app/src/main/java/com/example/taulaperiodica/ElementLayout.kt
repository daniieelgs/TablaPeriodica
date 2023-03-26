package com.example.taulaperiodica

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class ElementLayout (context: Context, attrs: AttributeSet? = null, style: Int = 0): ConstraintLayout(context, attrs, style){

    private lateinit var tvNumber: TextView
    private lateinit var tvSymbol: TextView
    private lateinit var tvName: TextView
    private lateinit var tvPes: TextView
    private lateinit var btnFavorite: ImageButton
    var favorite: Boolean = false
        set(value){

            btnFavorite.setImageResource(if(value) R.drawable.ic_baseline_star_iluminated_24 else R.drawable.ic_baseline_star_outline_24)

            field = value
        }

    private var data = Data(context)

    constructor(context: Context, attrs:AttributeSet? = null): this(context, attrs, 0){}

    var numberElement: Int
        get() = tvNumber.text.toString().toInt()
        set(value){

            if(!this::tvNumber.isInitialized) tvNumber = findViewById(R.id.tvNumber)
            tvNumber.text = value.toString()

        }

    var symbolElement: String
        get() = tvSymbol.text.toString()
        set(value){

            if(!this::tvSymbol.isInitialized) tvSymbol = findViewById(R.id.tvSymbol)
            tvSymbol.text = value

        }

    var nameElement: String
        get() = tvName.text.toString()
        set(value){

            if(!this::tvName.isInitialized) tvName = findViewById(R.id.tvName)
            tvName.text = value

        }

    var pesElement: Double
        get() = tvPes.text.toString().toDouble()
        set(value){

            if(!this::tvPes.isInitialized) tvPes = findViewById(R.id.tvPes)
            tvPes.text = value.toString().replace(".", context.getString(R.string.decimal_separator))

        }

    var categoryElement: String? = null
        set(value){

            setBackgroundColor((if(data.category_data.containsKey(value)) data.category_data[value]!! else data.category_data["unknown"]!!).color)

            field = value
        }

    var phaseElement: String? = null
        set(value){

            tvSymbol.setTextColor((if(data.phase_data.containsKey(value)) data.phase_data[value]!! else data.phase_data["unknown"]!!).color)

            field = value
        }

    fun buildElement(numberElement: Int, symbolElement: String, nameElement: String, pesElement: Double, categoryElement: String, phaseElement: String){
        this.numberElement = numberElement
        this.symbolElement = symbolElement
        this.nameElement = nameElement
        this.pesElement = pesElement
        this.categoryElement = categoryElement
        this.phaseElement = phaseElement
    }

    fun buildElement(element: Element) = buildElement(element.number.toInt(), element.symbol, element.name, element.atomic_mass, element.category, element.phase)

    fun setFavoriteListener(l: OnClickListener){

        btnFavorite = findViewById(R.id.btnFavorite)

        btnFavorite.setOnClickListener {

            favorite = !favorite

            l.onClick(it)
        }

    }

}