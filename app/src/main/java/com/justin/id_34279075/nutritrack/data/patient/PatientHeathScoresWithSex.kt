package com.justin.id_34279075.nutritrack.data.patient

/**
 * Represents health score of a patient but with sex included.
 */
data class PatientHeathScoresWithSex(

    val sex: String,

    val vegetableScore: Float,

    val fruitsScore: Float,

    val grainsAndCerealScore: Float,

    val wholeGrainsScore: Float,

    val meatAndAlternativesScore: Float,

    val dairyScore: Float,

    val waterScore: Float,

    val saturatedFatScore: Float,

    val unsaturatedFatScore: Float,

    val sodiumScore: Float,

    val sugarScore: Float,

    val alcoholScore: Float,

    val discretionaryFoodsScore: Float

)
