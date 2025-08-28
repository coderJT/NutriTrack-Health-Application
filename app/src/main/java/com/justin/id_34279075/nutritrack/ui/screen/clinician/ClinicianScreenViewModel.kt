package com.justin.id_34279075.nutritrack.ui.screen.clinician

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.justin.id_34279075.nutritrack.BuildConfig
import com.justin.id_34279075.nutritrack.data.helpers.convertToPDF
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ClinicianScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _averageMaleHEIFAScore = MutableLiveData<Float>()
    val averageMaleHEIFAScore: LiveData<Float> = _averageMaleHEIFAScore

    private val _averageFemaleHEIFAScore = MutableLiveData<Float>()
    val averageFemaleHEIFAScore: LiveData<Float> = _averageFemaleHEIFAScore

    private val _adminAuthenticated = MutableLiveData<Boolean>()
    val adminAuthenticated: LiveData<Boolean> = _adminAuthenticated

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    init {
        loadAverageMaleHEIFAScore()
        loadAverageFemaleHEIFAScore()
    }

    fun togglePassword(updatedPassword: String) {
        password = updatedPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    private fun loadAverageFemaleHEIFAScore() {
        viewModelScope.launch {

            val score = withContext(Dispatchers.IO) {
                patientRepository.getAverageFemaleHEIFAScore()
            }
            _averageFemaleHEIFAScore.value = score
        }
    }

    private fun loadAverageMaleHEIFAScore() {
        viewModelScope.launch {

            val score = withContext(Dispatchers.IO) {
                patientRepository.getAverageMaleHEIFAScore()
            }
            _averageMaleHEIFAScore.value = score
        }
    }

    fun getPatternsFromAllPatientScores() {

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val dataset = withContext(Dispatchers.IO) {
                    patientRepository.getAllPatientHealthScoresWithSex()
                }

                val formattedData = dataset.joinToString("\n\n") { score ->
                    """
                    Gender: ${score.sex}
                    Vegetable Score: ${score.vegetableScore}
                    Fruits Score: ${score.fruitsScore}
                    Grains & Cereal Score: ${score.grainsAndCerealScore}
                    Whole Grains Score: ${score.wholeGrainsScore}
                    Meat & Alternatives Score: ${score.meatAndAlternativesScore}
                    Dairy Score: ${score.dairyScore}
                    Water Score: ${score.waterScore}
                    Saturated Fat Score: ${score.saturatedFatScore}
                    Unsaturated Fat Score: ${score.unsaturatedFatScore}
                    Sodium Score: ${score.sodiumScore}
                    Sugar Score: ${score.sugarScore}
                    Alcohol Score: ${score.alcoholScore}
                    Discretionary Foods Score: ${score.discretionaryFoodsScore}
                    """.trimIndent()
                }

                val prompt = buildString {
                    append("Analyze the following patient dietary component scores and identify 3 interesting patterns based on gender:\n\n")
                    append(formattedData)
                }

                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(prompt)
                }

                response.text?.let { outputContent ->
                    Log.d("output", outputContent)
                    _message.value = outputContent
                }

                _isLoading.value = false

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }
        }
    }

    fun authenticateAdmin(password: String) {
        if (password == BuildConfig.adminPassword) {
            _adminAuthenticated.value = true
            _errorMessage.value = ""
        } else {
            _adminAuthenticated.value = false
            _errorMessage.value = "The provided password is incorrect. Please try again."
        }
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun generatePDF(context: Context): File? {
        return _message.value?.let { convertToPDF(context, it, "report") }
    }

    class ClinicianScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianScreenViewModel(context) as T
    }

}