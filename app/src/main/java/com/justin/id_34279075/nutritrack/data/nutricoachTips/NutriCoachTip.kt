package com.justin.id_34279075.nutritrack.data.nutricoachTips

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.justin.id_34279075.nutritrack.data.patient.Patient

/**
 * Represents a data class containing AI tips given to a specific patient.
 */
@Entity(
    tableName = "NutriCoachTips",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userID"],
            childColumns = ["patientID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NutriCoachTip (

    /**
     * Here we also assume that patientID is unique, therefore we don't need autoGenerate = true here.
     */
    @PrimaryKey
    val patientID: String,

    val tips: List<Tip>,
)

data class Tip(
    val text: String,
    val dateTime: String
)
