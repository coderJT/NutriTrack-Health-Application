package com.justin.id_34279075.nutritrack.data.patient

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PatientDAO {

    /**
     * Inserts a new Patient object into the database.
     *
     * @param patient The Patient object to be inserted.
     */
    @Insert
    suspend fun insert(patient: Patient)

    /**
     * Updates an existing Patient object in the database.
     *
     * @param patient The Patient object to be updated.
     */
    @Update
    suspend fun update(patient: Patient)

    /**
     * Retrieves all complete patient information.
     *
     * @return A LiveData emitting a list of all Patients in the database.
     */
    @Query("SELECT * FROM Patient ORDER BY userID ASC")
    fun getAllPatients(): LiveData<List<Patient>>

    /**
     * Retrieves all registered patient's userID(s) from the database.
     *
     * @return A LiveData emitting a list of all patient's userID(s) in the database.
     */
    @Query("SELECT userID FROM Patient WHERE name IS NOT NULL ORDER BY userID ASC")
    fun getAllRegisteredPatientsUserIDs(): LiveData<List<String>>

    /**
     * Retrieves all not registered patient's userID(s) from the database.
     *
     * @return A LiveData emitting a list of all patient's userID(s) in the database.
     */
    @Query("SELECT userID FROM Patient WHERE name IS NULL ORDER BY userID ASC")
    fun getAllNotRegisteredPatientsUserIDs(): LiveData<List<String>>


    /**
     * Retrieves a Patient object through userID.
     *
     * @param userID: userID value of a target patient.
     * @return Boolean value indicating whether patient exists by verifying his/her userID.
     */
    @Query("SELECT * FROM Patient WHERE userID = :userID")
    suspend fun getPatientByUserID(userID: String): Patient?

    /**
     * Retrieves a Patient object through userID and phone number.
     *
     * @param userID: userID value of a target patient.
     * @param phoneNumber: Phone number value of a target patient.
     * @return A Patient object which userID and phone number matches the given userID and
     *         phone number value, or 'null' if the patient is not found.
     */
    @Query("SELECT * FROM Patient WHERE userID = :userID AND phoneNumber = :phoneNumber")
    suspend fun getPatientByUserIDAndPhoneNumber(userID: String, phoneNumber: String): Patient?

    /**
     * Retrieves the basic information of a patient.
     *
     * @param userID: userID value of a target patient.
     * @return A PatientBasicInfo object of a patient which userID matches the given userID,
     *         or 'null' if the patient is not found.
     */
    @Query("SELECT userID, phoneNumber, name, sex FROM Patient WHERE userID = :userID")
    suspend fun getPatientBasicInfoByUserID(userID: String): PatientBasicInfo?

    /**
     * Retrieves the HEIFA score of a patient.
     *
     * @param userID: userID value of a target patient.
     * @return The HEIFA float value of a patient which userID matches the given userID,
     *         or 'null' if the patient is not found.
     */
    @Query("SELECT totalHEIFAScore FROM Patient WHERE userID = :userID")
    suspend fun getPatientTotalHEIFAScoreByUserID(userID: String): Float?

    /**
     * Retrieves the name of a patient.
     *
     * @param userID: userID value of a target patient.
     * @return The name of the patient which userID matches the given userID,
     *         or 'null' if the patient is not found.
     */
    @Query("SELECT name FROM Patient WHERE userID = :userID")
    suspend fun getPatientNameByUserID(userID: String): String?

    /**
     * Retrieves all health scores of a patient.
     *
     * @param userID: userID value of a target patient.
     * @return A PatientHealthScores object of a target patient which userID matches the given
     *         userID, or 'null' if the patient is not found.
     */
    @Query("""
        SELECT 
            vegetableScore, 
            fruitsScore, 
            grainsAndCerealScore, 
            wholeGrainsScore, 
            meatAndAlternativesScore, 
            dairyScore, 
            waterScore, 
            saturatedFatScore, 
            unsaturatedFatScore, 
            sodiumScore, 
            sugarScore, 
            alcoholScore, 
            discretionaryFoodsScore 
        FROM Patient
        WHERE userID = :userID
    """)
    suspend fun getPatientHealthScoresByUserID(userID: String): PatientHealthScores?

    /**
     * Retrieves all health scores of a patient along with his/her sex.
     *
     * @return A LiveData emitting a list of PatientHealthScoresWithSex object from all patients.
     */
    @Query("""
        SELECT 
            sex,
            vegetableScore, 
            fruitsScore, 
            grainsAndCerealScore, 
            wholeGrainsScore, 
            meatAndAlternativesScore, 
            dairyScore, 
            waterScore, 
            saturatedFatScore, 
            unsaturatedFatScore, 
            sodiumScore, 
            sugarScore, 
            alcoholScore, 
            discretionaryFoodsScore 
        FROM Patient
    """)
    fun getAllPatientHealthScoresWithSex(): List<PatientHeathScoresWithSex>

    /**
     * Retrieves the average HEIFA score of all male patients.
     *
     * @return The average HEIFA score based on all male patients HEIFA scores.
     */
    @Query("SELECT AVG(totalHEIFAScore) FROM Patient WHERE sex = 'Male'")
    suspend fun getAverageMaleHEIFAScore(): Float

    /**
     * Retrieves the average HEIFA score of all female patients.
     *
     * @return The average HEIFA score based on all female patients HEIFA scores.
     */
    @Query("SELECT AVG(totalHEIFAScore) FROM Patient WHERE sex = 'Female'")
    suspend fun getAverageFemaleHEIFAScore(): Float


    /**
     * Retrieves the fruit score (fruit serve size and fruit variation score) of a patient.
     *
     * @param userID: userID value of a target patient.
     * @return The PatientFruitScore entity value of a target patient which userID matches the given
     *        userID, or 'null' if the patient is not found.
     */
    @Query("SELECT fruitServeSize, fruitVariationScore FROM Patient WHERE userID = :userID")
    suspend fun getPatientFruitScoreByUserID(userID: String): PatientFruitScore?
}