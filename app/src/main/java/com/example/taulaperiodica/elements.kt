package com.example.taulaperiodica

class Element (
    val name: String,
    val appearance: String,
    val atomic_mass: Double,
    val boil: Double,
    val category: String,
    val density: Double,
    val discovered_by: String,
    val melt: Double,
    val molar_heat: Double,
    val named_by: String,
    val number: Long,
    val period: Long,
    val phase: String,
    val source: String,
    val bohrModelImage: String,
    val bohrModel3D: String,
    val spectralImg: String,
    val summary: String,
    val symbol: String,
    val xpos: Long,
    val ypos: Long,
    val shells: List<Long>,
    val electron_configuration: String,
    val electron_configuration_semantic: String,
    val electron_affinity: Double,
    val electronegativity_pauling: Double,
    val ionization_energies: List<Double>,
    val cpk_hex: String,
    val image: Image,
    var favorite: Boolean
): java.io.Serializable{
    data class Image (
        val title: String,
        val url: String,
        val attribution: String
    ): java.io.Serializable
}