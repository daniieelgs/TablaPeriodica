package com.example.taulaperiodica
import android.content.Context



class ElementReader {

    companion object{

        fun readElement(context: Context, fileName: String): List<Element>? = JsonReader.parseListFromAsset<Element>(context, fileName)

    }
}