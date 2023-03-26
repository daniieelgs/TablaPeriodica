package com.example.taulaperiodica

import android.content.Context
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonReader {

    companion object{
        fun readFromAsset(context: Context, fileName: String): String?{
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

        inline fun <reified T> parseListFromAsset(context: Context, fileName: String): List<T>? {

            val gson = Gson()

            val listType = object : TypeToken<List<T>>() {}.type

            return gson.fromJson(readFromAsset(context, fileName), listType)

        }
    }
}