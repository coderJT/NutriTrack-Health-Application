package com.justin.id_34279075.nutritrack.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategory
import com.justin.id_34279075.nutritrack.data.foodIntake.Persona
import com.justin.id_34279075.nutritrack.data.nutricoachTips.Tip
import com.justin.id_34279075.nutritrack.data.foodIntake.TimingQuestionWithAnswer

@RequiresApi(Build.VERSION_CODES.O)
/**
 * Converter utility for easy storage and retrieval to and from Room database.
 */
class Converter {

    private val gson = Gson()

    /**
     * Converts a List of FoodCategory enum to String.
     *
     * @param foodCategoryList List of FoodCategory enum to be converted.
     * @return [String] representation of the list of FoodCategory enums.
     */
    @TypeConverter
    fun fromFoodCategoryList(foodCategoryList: List<FoodCategory>): String {
        return gson.toJson(foodCategoryList)
    }

    /**
     * Converts a string representation of list of FoodCategory enums to List of FoodCategory enums.
     *
     * @param foodCategoryString: String representation of FoodCategory enums.
     * @return [List] of [FoodCategory] enums.
     */
    @TypeConverter
    fun toFoodCategoryList(foodCategoryString: String): List<FoodCategory> {
        val listType = object : TypeToken<List<FoodCategory>>() {}.type
        return gson.fromJson(foodCategoryString, listType)
    }

    /**
     * Converts a List of FoodCategory enum to String.
     *
     * @param timingList List of TimingQuestionWithAnswer data class to be converted.
     * @return [String] representation of the list of TimingQuestionWithAnswer data class.
     */
    @TypeConverter
    fun fromTimingQuestionWithAnswerList(timingList: List<TimingQuestionWithAnswer>): String {
        return gson.toJson(timingList)
    }

    /**
     * Converts a string representation of list of TimingQuestionWithAnswer data class
     * to List of TimingQuestionWithAnswer data class.
     *
     * @param timingListString: [String] representation of TimingQuestionWithAnswer list.
     * @return [List] of [TimingQuestionWithAnswer] data class.
     */
    @TypeConverter
    fun toTimingQuestionWithAnswerList(timingListString: String): List<TimingQuestionWithAnswer> {
        val listType = object : TypeToken<List<TimingQuestionWithAnswer>>() {}.type
        return gson.fromJson(timingListString, listType)
    }

    /**
     * Converts a Persona enum to String.
     *
     * @param persona: [Persona] enum to be converted.
     * @return [String] representation of the Persona.
     */
    @TypeConverter
    fun fromPersona(persona: Persona): String {
        return persona.name
    }

    /**
     * Converts a String representation of Persona enum to Persona enum.
     *
     * @param personaString [String] representation of Persona enum.
     * @return [Persona] enum.
     */
    @TypeConverter
    fun toPersonaType(personaString: String): Persona {
        return Persona.valueOf(personaString)
    }

    /**
     * Converts a List of String values into a String.
     *
     * @param stringList [List] of [String]s to be converted.
     * @return [String] representation of the list of strings.
     */
    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        return Gson().toJson(stringList)
    }

    /**
     * Converts a String representation of list of strings into a list of strings.
     *
     * @param listString [String] representation of a list of strings.
     * @return [List] of [String]s.
     */
    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(listString, listType)
    }

    /**
     * Converts a List of Tip values into a String.
     *
     * @param tipList [List] of tip to be converted.
     * @return [String] representation of the list of tip.
     */
    @TypeConverter
    fun fromTipList(tipList: List<Tip>): String {
        return Gson().toJson(tipList)
    }

    /**
     * Converts a String representation of list of tip into a list of tip.
     *
     * @param tipListString [String] representation of a list of tip.
     * @return [List] of [Tip]s.
     */
    @TypeConverter
    fun toTipList(tipListString: String): List<Tip> {
        val listType = object : TypeToken<List<Tip>>() {}.type
        return Gson().fromJson(tipListString, listType)
    }

}