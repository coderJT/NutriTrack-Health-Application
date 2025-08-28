package com.justin.id_34279075.nutritrack.data.foodIntake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodIntakeDAO {

    /**
     * Inserts a new FoodIntake object into the FoodIntake database.
     *
     * @param foodIntake: FoodIntake object to be inserted into the database.
     */
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    /**
     * Updates an existing FoodIntake object into the FoodIntake database.
     *
     * @param foodIntake: FoodIntake object to be updated in the database.
     */
    @Update
    suspend fun update(foodIntake: FoodIntake)

    /**
     * Retrieves a FoodIntake object based on patientID.
     *
     * @param patientID: PatientID of the FoodIntake object to be retrieved.
     * @return [FoodIntake] object which patientID value matches the patientID value given.
     */
    @Query("SELECT * FROM FoodIntake WHERE patientID = :patientID")
    suspend fun getFoodIntakeByPatientID(patientID: String): FoodIntake?
}