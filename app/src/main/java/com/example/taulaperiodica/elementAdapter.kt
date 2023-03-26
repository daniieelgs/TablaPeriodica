package com.example.taulaperiodica

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class elementAdapter(private var elements: List<Element>, val context: Context): RecyclerView.Adapter<elementAdapter.ViewHolder>() {

    private var resultElementActivity = (context as AppCompatActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        val element = data!!.getSerializableExtra(InfoElement.EXTRA_ELEMENT) as Element

        elements.first { it.number == element.number }.favorite = element.favorite
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycle_row_element, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elements[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = elements.size

    fun getElementsList() = elements

    fun update(elements: List<Element>){
        this.elements = elements
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val boil = view.findViewById<TextView>(R.id.tvBoil)
        val category = view.findViewById<TextView>(R.id.tvCategory)
        val molar = view.findViewById<TextView>(R.id.tvMolarHeat)
        val period = view.findViewById<TextView>(R.id.tvPeriod)
        val phase = view.findViewById<TextView>(R.id.tvPhase)

        val elementLayout = view.findViewById<ElementLayout>(R.id.elementViewOnce)

        fun bind(element: Element, context: Context){

            elementLayout.nameElement = element.name
            elementLayout.symbolElement = element.symbol
            elementLayout.numberElement = element.number.toInt()
            elementLayout.pesElement = element.atomic_mass
            elementLayout.categoryElement = element.category
            elementLayout.phaseElement = element.phase

            val data = Data(context)

            boil.text = element.boil.toString()
            category.text = (data.category_data[element.category] ?: data.category_data["unknown"])!!.text
            molar.text = element.molar_heat.toString()
            period.text = element.period.toString()
            phase.text = (data.phase_data[element.phase] ?: data.phase_data["unknown"])!!.text

            elementLayout.setFavoriteListener { element.favorite = elementLayout.favorite }

            elementLayout.favorite = element.favorite

            itemView.setOnClickListener {
                val intent = Intent(context, InfoElement::class.java)
                intent.putExtra(InfoElement.EXTRA_ELEMENT, element)
                resultElementActivity.launch(intent)
            }

        }

    }
}