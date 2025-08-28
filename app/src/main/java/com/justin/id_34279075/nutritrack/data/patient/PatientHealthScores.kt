package com.justin.id_34279075.nutritrack.data.patient

/**
 * Represents health score of a patient.
 *
 * By selecting necessary values only, it encourages less recomposition due to change in
 * unrelated data values (if applicable).
 */
data class PatientHealthScores(

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

/**
 * Maps a given string to its respective attribute's value
 */
fun PatientHealthScores.toLabeledMap(): Map<String, Float> {
    return mapOf(
        "Vegetables" to vegetableScore,
        "Fruits" to fruitsScore,
        "Grains & Cereals" to grainsAndCerealScore,
        "Whole Grains" to wholeGrainsScore,
        "Meat & Alternatives" to meatAndAlternativesScore,
        "Dairy" to dairyScore,
        "Water" to waterScore,
        "Saturated Fats" to saturatedFatScore,
        "Unsaturated Fats" to unsaturatedFatScore,
        "Sodium" to sodiumScore,
        "Sugar" to sugarScore,
        "Alcohol" to alcoholScore,
        "Discretionary Foods" to discretionaryFoodsScore
    )
}
