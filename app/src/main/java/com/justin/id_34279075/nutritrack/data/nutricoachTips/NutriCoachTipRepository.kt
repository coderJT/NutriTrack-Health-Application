package com.justin.id_34279075.nutritrack.data.nutricoachTips

import android.content.Context
import com.justin.id_34279075.nutritrack.data.database.NutriTrackDatabase

class NutriCoachTipRepository(context: Context) {

    private val nutriCoachTipDAO = NutriTrackDatabase.getDatabase(context).nutriCoachTipDAO()

    /**
     * Adds a new NutriCoachTip entity into the NutriCoachTips table in the database.
     *
     * @param nutriCoachTip NutriCoachTip entity to be added.
     * @throws IllegalArgumentException If NutriCoachTip entity to be added already exists (verified through
     *         patientID) since patientID is the primary key of NutriCoachTip entity.
     */
    suspend fun insert(nutriCoachTip: NutriCoachTip) {

        nutriCoachTipDAO.insert(nutriCoachTip)
    }

    /**
     * Updates an existing NutriCoachTip entity in the NutriCoachTips table in the database.
     *
     * @param nutriCoachTip NutriCoachTip entity to be updated.
     * @throws IllegalArgumentException If NutriCoachTip to be updated does not exists (verified through
     *         patientID) since patientID is the primary key of FoodIntake entity.
     */
    suspend fun update(nutriCoachTip: NutriCoachTip) {

        nutriCoachTipDAO.update(nutriCoachTip)
    }

    /**
     * Retrieves the tips from a NutriCoachTip entity with patientID matching the given patientID value.
     *
     * @param patientID patientID value of the NutriCoachTip entity to be matched.
     * @return [List] of tips in [String] format.
     */
    suspend fun getTipsList(patientID: String): List<Tip>? {
        return nutriCoachTipDAO.getTipsListForPatientID(patientID)?.tips
    }

    /**
     * Retrieves the NutriCoachTip entity with patientID matching the given patientID value.
     *
     * @param patientID patientID value of the NutriCoachTip entity to be matched.
     * @return NutriCoachTip entity of target patient.
     */
    suspend fun getTips(patientID: String): NutriCoachTip? {
        return nutriCoachTipDAO.getTipsListForPatientID(patientID)
    }

}