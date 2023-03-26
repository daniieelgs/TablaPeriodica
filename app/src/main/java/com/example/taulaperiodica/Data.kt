package com.example.taulaperiodica

import android.content.Context

class Data (context: Context){

    data class data(var color: Int, var text: String)

    var category_data: Map<String, data>
        private set
    var phase_data: Map<String, data>
        private set

    init {

        category_data = mapOf(
            Pair("noble gas",
                data(
                    context.getColor(R.color.bg_noble_gas),
                    context.getString(R.string.noble_gas)
                )
            ),
            Pair("alkali metal",
                data(
                    context.getColor(R.color.bg_alkali_metal),
                    context.getString(R.string.alkali_metal)
                )
            ),
            Pair("alkaline earth metal",
                data(
                    context.getColor(R.color.bg_alkaline_earth_metal),
                    context.getString(R.string.alkaline_earth_metal)
                )
            ),
            Pair("metalloid",
                data(
                    context.getColor(R.color.bg_semimetal),
                    context.getString(R.string.semimetal)
                )
            ),
            Pair("polyatomic nonmetal",
                data(
                    context.getColor(R.color.bg_other_metal),
                    context.getString(R.string.other_metal)
                )
            ),
            Pair("diatomic nonmetal",
                data(
                    context.getColor(R.color.bg_other_metal),
                    context.getString(R.string.other_metal)
                )
            ),
            Pair("post-transition metal",
                data(
                    context.getColor(R.color.bg_post_transition_metal),
                    context.getString(R.string.post_transition_metal)
                )
            ),
            Pair("transition metal",
                data(
                    context.getColor(R.color.bg_transition_metal),
                    context.getString(R.string.transition_metal)
                )
            ),
            Pair("lanthanide",
                data(
                    context.getColor(R.color.bg_lanthanide),
                    context.getString(R.string.lanthanide)
                )
            ),
            Pair("actinide",
                data(
                    context.getColor(R.color.bg_actinide),
                    context.getString(R.string.actinide)
                )
            ),
            Pair("unknown",
                data(
                    context.getColor(R.color.bg_unknown),
                    context.getString(R.string.unknown)
                )
            ),
        )

        phase_data = mapOf(
            Pair("Gas",
                data(
                    context.getColor(R.color.phase_gas),
                    context.getString(R.string.gas)
                )
            ),
            Pair("Solid",
                data(
                    context.getColor(R.color.phase_solid),
                    context.getString(R.string.solid)
                )
            ),
            Pair("Liquid",
                data(
                    context.getColor(R.color.phase_liquid),
                    context.getString(R.string.liquid)
                )
            ),
            Pair("Unknown",
                data(
                    context.getColor(R.color.phase_unkown),
                    context.getString(R.string.unknown)
                )
            ),
        )

    }

}