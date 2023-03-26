package com.example.taulaperiodica

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class SnackbarCustom {

    companion object{

        var snackabr_time = Snackbar.LENGTH_SHORT

        fun snackBarSimple(view: View, message: String, time: Int = snackabr_time) = Snackbar.make(view, message, time)

        fun snackBarError(context: Activity, message: String, time: Int = snackabr_time, view: View = context.findViewById(android.R.id.content)): Snackbar{
            val sb = snackBarSimple(view, message, time)

            with(context){
                sb.setBackgroundTint(getColor(R.color.snackbar_error))
                sb.setTextColor(getColor(R.color.snackbar_error_text))

                val sbv: View = sb.view

                var tv: TextView =  sbv.findViewById(com.google.android.material.R.id.snackbar_text)
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.snackbar_error))
            }

            return sb
        }

        fun showSnackbarError(context: Activity, message: String, time: Int = snackabr_time, view: View = context.findViewById(android.R.id.content)) = snackBarError(context, message, time, view).show()


    }

}