package com.justin.id_34279075.nutritrack.data.patient

/**
 * Represents the most basic personal information of a patient.
 *
 * By selecting necessary values only, it encourages less recomposition due to change in
 * unrelated data values (if applicable).
 */
data class PatientBasicInfo(

    val userID: String,

    val phoneNumber: String,

    val name: String,

    val sex: String

)
