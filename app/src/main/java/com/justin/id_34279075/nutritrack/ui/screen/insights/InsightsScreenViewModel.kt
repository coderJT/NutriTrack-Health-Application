package com.justin.id_34279075.nutritrack.ui.screen.insights

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategoryInfo
import com.justin.id_34279075.nutritrack.data.patient.PatientHealthScores
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsightsScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _foodInfo = MutableLiveData<FoodCategoryInfo?>()
    val foodInfo: LiveData<FoodCategoryInfo?> get() = _foodInfo

    private val _totalHEIFAScore  = MutableLiveData<Float>()
    val totalHEIFAScore: LiveData<Float> = _totalHEIFAScore

    private val _scores  = MutableLiveData<PatientHealthScores>()
    val scores: LiveData<PatientHealthScores> = _scores

    init {
        val userID = AuthManager.getCurrentUserID()
        if (userID != null) {
            getPatientHEIFAScore(userID)
            getPatientHealthScores(userID)
        }
    }

    fun selectFoodInfo(foodCategoryInfo: FoodCategoryInfo?) {
        _foodInfo.value = foodCategoryInfo
    }

    /**
     * This function retrieves a patient's HEIFA score.
     *
     * @param userID: userID of the patient.
     */
    fun getPatientHEIFAScore(userID: String){

        viewModelScope.launch {
            try {
                val score = withContext(Dispatchers.IO) {
                    patientRepository.getPatientTotalHEIFAScoreByUserID(userID)
                }
                _totalHEIFAScore.value = score

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }
        }
    }

    /**
     * This function retrieves a patient's health score.
     *
     * @param userID: userID of the patient.
     */
    fun getPatientHealthScores(userID: String) {

        viewModelScope.launch {
            try {
                val score = withContext(Dispatchers.IO) {
                    patientRepository.getPatientHealthScoresByUserID(userID)
                }
                _scores.value = score

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }
        }
    }

    class InsightsScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            InsightsScreenViewModel(context) as T
    }

}