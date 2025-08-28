package com.justin.id_34279075.nutritrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodIntake
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodIntakeDAO
import com.justin.id_34279075.nutritrack.data.nutricoachTips.NutriCoachTip
import com.justin.id_34279075.nutritrack.data.nutricoachTips.NutriCoachTipDAO
import com.justin.id_34279075.nutritrack.data.patient.Patient
import com.justin.id_34279075.nutritrack.data.patient.PatientDAO

@Database(
    entities = [Patient::class, FoodIntake::class, NutriCoachTip::class],
    version = 1,
)
@TypeConverters(Converter::class)
abstract class NutriTrackDatabase: RoomDatabase() {

    /**
     * Access to PatientDAO interface for performing database related operation on Patient entities.
     * @return [PatientDAO] instance.
     */
    abstract fun patientDAO(): PatientDAO

    /**
     * Access to FoodIntakeDAO interface for performing database related operation on FoodIntake entities.
     * @return [FoodIntakeDAO] instance.
     */
    abstract fun foodIntakeDAO(): FoodIntakeDAO

    /**
     * Access to PatientDAO interface for performing database related operation on NutriCoachTip entities.
     * @return [NutriCoachTipDAO] instance.
     */
    abstract fun nutriCoachTipDAO(): NutriCoachTipDAO

    companion object {

        // Singleton format
        @Volatile
        private var Instance: NutriTrackDatabase? = null

        /**
         * Retrieves the singleton database.
         * Creates a new database instance if not existing, else return the relevant database.
         * @param context Context of application.
         * @return [NutriTrackDatabase] instance.
         */
        fun getDatabase(context: Context): NutriTrackDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NutriTrackDatabase::class.java, "nutritrack_database")
                    // Temporary for development to drop and recreate db when version changes
                    .fallbackToDestructiveMigration().build().also{ Instance = it }
            }
        }
    }
}