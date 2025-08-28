package com.justin.id_34279075.nutritrack.data.foodIntake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.justin.id_34279075.nutritrack.data.patient.Patient

/**
 * Handles the creation of the FoodIntake entity.
 */
@Entity(
    tableName = "FoodIntake",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userID"],
            childColumns = ["patientID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake(
    /**
     * Here we also assume that patientID is unique, therefore we don't need to use autogenerate.
     */
    @PrimaryKey
    val patientID: String,

    val foodCategories: List<FoodCategory>? = mutableListOf(),

    val persona: Persona? = null,

    val timingQuestions: List<TimingQuestionWithAnswer>? = mutableListOf()
)