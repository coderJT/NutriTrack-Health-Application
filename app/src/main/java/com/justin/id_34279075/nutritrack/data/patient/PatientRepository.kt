package com.justin.id_34279075.nutritrack.data.patient

import android.content.Context
import androidx.lifecycle.LiveData
import com.justin.id_34279075.nutritrack.data.database.NutriTrackDatabase
import com.justin.id_34279075.nutritrack.data.helpers.hashPassword

class PatientRepository(context: Context) {

    private val patientDAO = NutriTrackDatabase.getDatabase(context).patientDAO()

    /**
     * Adds a new Patient entity into the Patient table in the database.
     *
     * @param patient Patient entity to be added.
     * @throws IllegalArgumentException If Patient entity to be added already exists (verified through
     *         userID) since userID is the primary key of Patient entity.
     */
    suspend fun insert(patient: Patient)  {

        // Checks if patient already exists by checking userID
        val existingPatient = patientDAO.getPatientByUserID(patient.userID)

        if (existingPatient != null) {
            throw IllegalArgumentException("Patient with userID ${patient.userID} already exists.")
        }

        patientDAO.insert(patient)
    }

    /**
     * Updates an existing patient from the Patient table in the database.
     *
     * @param patient Patient entity to be updated.
     * @throws IllegalArgumentException If patient to be updated does not exists (verified through
     *         userID) since userID is the primary key of Patient entity.
     */
    suspend fun update(patient: Patient)  {

        // Checks if patient already exists by checking userID
        val existingPatient = patientDAO.getPatientByUserID(patient.userID)
            ?: throw IllegalArgumentException("Patient with userID: ${patient.userID} does not exist.")

        patientDAO.update(patient)
    }

    /**
     * Retrieves a Patient entity through userID and password.
     *
     * @param userID userID value of the target Patient entity.
     * @param password password value of the target Patient entity.
     *
     * @return Patient entity which userID and password matches the given userID and password given.
     * @throws IllegalArgumentException If incorrect credentials are provided (password given
     *         does not match the password of the Patient with userID equals the provided userID).
     */
    suspend fun getPatientByUserIDAndPassword(userID: String, password: String): Patient {

        val patient = patientDAO.getPatientByUserID(userID)
            ?: throw IllegalArgumentException("Incorrect userID provided.")

        if (patient.password == null) {
            throw IllegalArgumentException("User not registered.")
        }

        val isPasswordValid = hashPassword(password) == patient.password

        if (!isPasswordValid) {
            throw IllegalArgumentException("Incorrect credentials provided.")
        }

        return patient
    }

    /**
     * Retrieves a Patient entity through userID and phone number.
     *
     * @return Patient entity which userID and password matches the given userID and phone number given.
     * @throws IllegalArgumentException If incorrect credentials are provided (phone number given
     *         does not match the phone number of the Patient with userID equals the provided userID).
     */
    suspend fun getPatientByUserIDAndPhoneNumber(userID: String, phoneNumber: String): Patient{

        val patient = patientDAO.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
            ?: throw IllegalArgumentException("Patient Not Found.")

        return patient
    }

    /**
     * Updates an existing patient from the Patient table in the database.
     *
     * @param userID userID of the target patient to be updated.
     * @param phoneNumber Phone number of the target patient to be updated.
     * @param name Name of the patient to be set.
     * @param password Password of the patient's account to be set.
     * @throws IllegalArgumentException If incorrect credentials are provided (phone number given
     *         does not match the phone number of the Patient with userID equals the provided userID).
     */
    suspend fun updatePatientNameAndPassword(
        userID: String, phoneNumber: String, name: String, password: String) {

        // Retrieves patient which credentials matches, else null
        val patient = patientDAO.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
            ?: throw IllegalArgumentException("Patient Not Found.")

        val updatedPatient = patient.copy(
            name = name,
            password = hashPassword(password)
        )

        update(updatedPatient)
    }

    /**
     * Updates an existing patient from the Patient table in the database.
     *
     * @param userID userID of the target patient to be updated.
     * @param phoneNumber Phone number of the target patient to be updated.
     * @param password Password of the patient's account to be set.
     * @throws IllegalArgumentException If incorrect credentials are provided (phone number given
     *         does not match the phone number of the Patient with userID equals the provided userID).
     */
    suspend fun updatePatientPassword(
        userID: String, phoneNumber: String, password: String) {

        // Retrieves patient which credentials matches, else null
        val patient = patientDAO.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
            ?: throw IllegalArgumentException("Patient Not Found.")

        val updatedPatient = patient.copy(
            password = hashPassword(password)
        )

        update(updatedPatient)
    }

    /**
     * Retrieve all registered patient's user IDs.
     *
     * @return LiveData that emits a list of strings which are the userIDs.
     */
    fun getAllRegisteredPatientUserIDs(): LiveData<List<String>> = patientDAO.getAllRegisteredPatientsUserIDs()

    /**
     * Retrieve all not registered patient's user IDs.
     *
     * @return LiveData that emits a list of strings which are the userIDs.
     */
    fun getAllNotRegisteredPatientUserIDs(): LiveData<List<String>> = patientDAO.getAllNotRegisteredPatientsUserIDs()

    /**
     * Retrieves a patient's total HEIFA Score based on userID.
     *
     * @param userID userID of the target patient.
     * @return Total HEIFA Score of the patient whose userID matches the given userID.
     * @throws IllegalArgumentException If patient with the userID doesn't exist.
     */
    suspend fun getPatientTotalHEIFAScoreByUserID(userID: String): Float {

        val patientTotalHEIFAScore = patientDAO.getPatientTotalHEIFAScoreByUserID(userID)
            ?: throw IllegalArgumentException("Patient with userID: $userID does not exist.")

        return patientTotalHEIFAScore
    }

    /**
     * Retrieves a patient's health scores based on userID.
     *
     * @param userID userID of the target patient.
     * @return PatientHealthScore entity from the patient whose userID matches the given userID.
     * @throws IllegalArgumentException If patient with the userID doesn't exist.
     */
    suspend fun getPatientHealthScoresByUserID(userID: String): PatientHealthScores {

        val patientHealthScores = patientDAO.getPatientHealthScoresByUserID(userID)
            ?: throw IllegalArgumentException("Patient with userID: $userID does not exist.")

        return patientHealthScores
    }

    /**
     * Retrieves a patient's basic info based on userID.
     *
     * @param userID userID of the target patient.
     * @return PatientBasicInfo entity from the patient whose userID matches the given userID.
     * @throws IllegalArgumentException If patient with the userID doesn't exist.
     */
    suspend fun getPatientBasicInfoByUserID(userID: String): PatientBasicInfo {

        val patientBasicInfo = patientDAO.getPatientBasicInfoByUserID(userID)
            ?: throw IllegalArgumentException("Patient with userID: $userID does not exist.")

        return patientBasicInfo
    }

    /**
     * Retrieves a patient's fruit scores based on userID.
     *
     * @param userID userID of the target patient.
     * @return Fruit score of the patient whose userID matches the given userID.
     * @throws IllegalArgumentException If patient with the userID doesn't exist.
     */
    suspend fun getPatientFruitScoreByUserID(userID: String): PatientFruitScore {

        val patientFruitScore = patientDAO.getPatientFruitScoreByUserID(userID)
            ?: throw IllegalArgumentException("Patient with userID: $userID does not exist.")

        return patientFruitScore
    }

    /**
     * Retrieves the average HEIFA score of male patients.
     *
     * @return Average HEIFA score of male patients.
     */
    suspend fun getAverageMaleHEIFAScore(): Float = patientDAO.getAverageMaleHEIFAScore()

    /**
     * Retrieves the average HEIFA score of female patients.
     *
     * @return Average HEIFA score of female patients.
     */
    suspend fun getAverageFemaleHEIFAScore(): Float = patientDAO.getAverageFemaleHEIFAScore()

    /**
     * Retrieves all patient's health scores along with their sex.
     *
     * @return PatientHealthScoreWithSex entity from all patients.
     */
    fun getAllPatientHealthScoresWithSex(): List<PatientHeathScoresWithSex> =
        patientDAO.getAllPatientHealthScoresWithSex()

}