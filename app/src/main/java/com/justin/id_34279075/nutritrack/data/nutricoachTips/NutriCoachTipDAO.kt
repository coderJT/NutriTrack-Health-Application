package com.justin.id_34279075.nutritrack.data.nutricoachTips

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NutriCoachTipDAO {

    /**
     * Inserts a new NutriCoachTip object into the database.
     *
     * @param nutriCoachTip The NutriCoachTip object to be inserted.
     */
    @Insert
    suspend fun insert(nutriCoachTip: NutriCoachTip)

    /**
     * Updates an existing NutriCoachTip object in the database.
     *
     * @param nutriCoachTip The NutriCoachTip object to be updated.
     */
    @Update
    suspend fun update(nutriCoachTip: NutriCoachTip)

    /**
     * Retrieves list of tips generated for a specific patient through patientID given.
     *
     * @param patientID patientID value of the NutriCoachTips object.
     * @return [NutriCoachTip] entity of the target patient.
     */
    @Query("SELECT * FROM NutriCoachTips WHERE patientID = :patientID")
    suspend fun getTipsListForPatientID(patientID: String): NutriCoachTip?
}