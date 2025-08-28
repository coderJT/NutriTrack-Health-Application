package com.justin.id_34279075.nutritrack.data.helpers

import android.content.Context
import com.justin.id_34279075.nutritrack.data.patient.Patient
import com.justin.id_34279075.nutritrack.data.patient.PatientDAO
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Helper method that extracts data from a csv file.
 *
 * @param context: Context
 * @return [List] of [List] of [String]s where each inner list represents a patient information,
 * which values stored as a String.
 */
fun extractCSVData(context: Context): List<List<String>> {

    val assets = context.assets
    val inputStream = assets.open("data.csv")
    val reader = BufferedReader(InputStreamReader(inputStream))
    val patientData = mutableListOf<List<String>>()

    reader.readLine() // Ignore first row (header)

    reader.forEachLine {
        val line = it.split(",")
        patientData.add(line)
    }

    reader.close()
    return patientData
}

/**
 * Loads csv extracted into the database.
 *
 * @param context: Context
 * @param patientDAO: PatientDAO instance
 */
suspend fun loadCSVIntoDatabase(context: Context, patientDAO: PatientDAO) {

    val csvData = extractCSVData(context)

    csvData.forEach { row ->

        val sex = row[2]

        val genderScores = if (sex == "Male") 0 else 1

        val patient = Patient(
            userID = row[1],
            phoneNumber = row[0],
            sex = sex,
            vegetableScore = row[8 + genderScores].toFloat(),
            fruitsScore = row[19 + genderScores].toFloat(),
            grainsAndCerealScore = row[29 + genderScores].toFloat(),
            wholeGrainsScore = row[33 + genderScores].toFloat(),
            meatAndAlternativesScore = row[36 + genderScores].toFloat(),
            dairyScore = row[40 + genderScores].toFloat(),
            waterScore = row[49 + genderScores].toFloat(),
            saturatedFatScore = row[57 + genderScores].toFloat(),
            unsaturatedFatScore = row[60 + genderScores].toFloat(),
            sodiumScore = row[43 + genderScores].toFloat(),
            sugarScore = row[54 + genderScores].toFloat(),
            alcoholScore = row[46 + genderScores].toFloat(),
            discretionaryFoodsScore = row[5 + genderScores].toFloat(),
            totalHEIFAScore = row[3 + genderScores].toFloat(),
            fruitServeSize = row[21].toFloat(),
            fruitVariationScore = row[22].toFloat()
        )

        patientDAO.insert(patient)
    }
}

