package com.justin.id_34279075.nutritrack.data.foodIntake

import android.content.Context
import com.justin.id_34279075.nutritrack.data.database.NutriTrackDatabase

class FoodIntakeRepository(context: Context) {

    private val foodIntakeDAO = NutriTrackDatabase.getDatabase(context).foodIntakeDAO()

    /**
     * Adds a new FoodIntake entity into the FoodIntake table in the database.
     *
     * @param foodIntake FoodIntake entity to be added.
     * @throws IllegalArgumentException If foodIntake entity to be added already exists (verified through
     *         patientID) since patientID is the primary key of FoodIntake entity.
     */
    suspend fun insert(foodIntake: FoodIntake) {

        // Checks if FoodIntake entity already exists by checking patientID
        val existingFoodIntake = foodIntakeDAO.getFoodIntakeByPatientID(foodIntake.patientID)

        if (existingFoodIntake != null) {
            throw IllegalArgumentException("FoodIntake for patientID ${foodIntake.patientID} already exist.")
        }

        foodIntakeDAO.insert(foodIntake)
    }

    /**
     * Updates an existing FoodIntake entity in the FoodIntake table in the database.
     *
     * @param foodIntake FoodIntake entity to be updated.
     * @throws IllegalArgumentException If FoodIntake entity to be updated does not exists (verified through
     *         patientID) since patientID is the primary key of FoodIntake entity.
     */
    suspend fun update(foodIntake: FoodIntake) {

        // Checks if FoodIntake entity already exists by checking patientID
        val existingFoodIntake = foodIntakeDAO.getFoodIntakeByPatientID(foodIntake.patientID)
            ?: throw IllegalArgumentException("FoodIntake for patientID ${foodIntake.patientID} does not exist.")

        foodIntakeDAO.update(foodIntake)
    }

    /**
     * Retrieves the FoodIntake entity through a given patientID.
     *
     * @param patientID patientID of the target FoodIntake instance.
     * @return [FoodIntake] entity of target patient.
     * @throws IllegalArgumentException If FoodIntake entity to be retrieved does not exists (verified through
     *         patientID) since patientID is the primary key of FoodIntake entity.
     */
    suspend fun getFoodIntakeByPatientID(patientID: String): FoodIntake {

        val foodIntake = foodIntakeDAO.getFoodIntakeByPatientID(patientID)
            ?: throw IllegalArgumentException("FoodIntake with patientID: $patientID does not exist.")

        return foodIntake
    }
}