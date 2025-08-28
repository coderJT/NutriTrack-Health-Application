package com.justin.id_34279075.nutritrack.ui.screen.nutricoach

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodIntakeRepository
import com.justin.id_34279075.nutritrack.data.network.FruitResponse
import com.justin.id_34279075.nutritrack.data.network.api.FruityViceAPI
import com.justin.id_34279075.nutritrack.data.network.api.PicsumAPI
import com.justin.id_34279075.nutritrack.data.nutricoachTips.NutriCoachTip
import com.justin.id_34279075.nutritrack.data.nutricoachTips.NutriCoachTipRepository
import com.justin.id_34279075.nutritrack.data.nutricoachTips.Tip
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import com.justin.id_34279075.nutritrack.data.patient.toLabeledMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NutriCoachScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)
    private val foodIntakeRepository: FoodIntakeRepository = FoodIntakeRepository(context)
    private val nutriCoachTipsRepository: NutriCoachTipRepository = NutriCoachTipRepository(context)

    private val _fruitScoreOptimal = MutableLiveData<Boolean?>()
    val fruitScoreOptimal: LiveData<Boolean?> get() = _fruitScoreOptimal

    private val _fruit = MutableLiveData<FruitResponse?>()
    val fruit: LiveData<FruitResponse?> get() = _fruit

    private val _fruitName = MutableLiveData<String?>()
    val fruitName: LiveData<String?> get() = _fruitName

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: MutableLiveData<String?> = _imageUrl

    private val _tips = MutableLiveData<NutriCoachTip?>()
    val tips: MutableLiveData<NutriCoachTip?> get() = _tips

    private val _tip = MutableLiveData<String?>()
    val tip: LiveData<String?> get() = _tip

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isTipLoading = MutableLiveData<Boolean>(false)
    val isTipLoading: LiveData<Boolean> = _isTipLoading

    private val _isFruitLoading = MutableLiveData<Boolean>(false)
    val isFruitLoading: LiveData<Boolean> = _isFruitLoading

    var query by mutableStateOf("")
        private set

    var showTips by mutableStateOf(false)
        private set

    private val fruitApi = Retrofit.Builder()
        .baseUrl("https://www.fruityvice.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FruityViceAPI::class.java)

    private val imageApi = Retrofit.Builder()
        .baseUrl("https://picsum.photos/")
        .build()
        .create(PicsumAPI::class.java)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun getRandomImage() {
        viewModelScope.launch {
            val response = imageApi.getRandomImage()
            if (response.isSuccessful) {
                val redirectedUrl = response.raw().request.url.toString()
                _imageUrl.postValue(redirectedUrl)
            }
        }
    }

    fun toggleShowTips() {
        showTips = !showTips
    }

    fun toggleQuery(updatedQuery: String) {
        query = updatedQuery
    }

    init {
        getRandomImage()
        val userID = AuthManager.getCurrentUserID()
        if (userID != null) {
            getPatientFruitScoreByUserID(userID)
            getTipsForPatient(userID)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendPrompt(userID: String) {
        viewModelScope.launch {
            _isTipLoading.value = true
            try {
                // Construct the patient health scores part of the prompt
                val patientHealthScores = withContext(Dispatchers.IO) {
                    patientRepository.getPatientHealthScoresByUserID(userID)
                }
                val labeledScores = patientHealthScores.toLabeledMap()

                val scoresDescriptionPrompt = labeledScores.entries.joinToString(separator = "\n") {
                    "${it.key}: ${it.value}"
                }

                // Next, we construct the patient food intake part of the prompt
                val foodIntake = withContext(Dispatchers.IO) {
                    foodIntakeRepository.getFoodIntakeByPatientID(userID)
                }

                val foodIntakeDescriptionPrompt = buildString {
                    append("\nFood Intake Details:")
                    append("\nBiggest Meal Time: ${foodIntake.timingQuestions?.get(0) ?: "Not Available"}")
                    append("\nSleep Time: ${foodIntake.timingQuestions?.get(1) ?: "Not Available"}")
                    append("\nWake Up Time: ${foodIntake.timingQuestions?.get(2) ?: "Not Available"}")

                    foodIntake.foodCategories?.let { categories ->
                        append("\nFood Categories: ${categories.joinToString(", ") { it.name }}")
                    } ?: append("\nFood Categories: Not Available")

                    foodIntake.persona?.let { persona ->
                        append("\nPersona: ${persona.name}, Persona description: ${persona.description}")
                    } ?: append("\nPersona: Not Available")
                }

                val prompt = buildString {
                    append("Generate a short encouraging message to help someone improve their food intake. ")
                    append("The patient's health score info are:\n")
                    append(scoresDescriptionPrompt)
                    append("\n\n")
                    append("The patient's food intake info are:\n")
                    append(foodIntakeDescriptionPrompt)
                }

                val response = generativeModel.generateContent(prompt)

                response.text?.let { outputContent ->
                    _tip.value = outputContent.trim()
                    addTipToDB(userID, outputContent)
                }

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }
            _isTipLoading.value = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTipToDB(patientID: String, tip: String) {
        viewModelScope.launch {
            val currentTips: List<Tip>? = nutriCoachTipsRepository.getTipsList(patientID)

            val currentDateTime = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            )

            val currentTip = Tip(tip, currentDateTime)
            val updatedTips: List<Tip> = if (currentTips != null) currentTips + currentTip else listOf(currentTip)

            val nutriCoachTip = NutriCoachTip(
                patientID = patientID,
                tips = updatedTips,
            )

            if (currentTips == null) {
                nutriCoachTipsRepository.insert(nutriCoachTip)
            } else {
                nutriCoachTipsRepository.update(nutriCoachTip)
            }

            getTipsForPatient(patientID)
        }
    }

    fun getTipsForPatient(patientID: String) {
        viewModelScope.launch {
            val currentTips = nutriCoachTipsRepository.getTips(patientID)
            _tips.value = currentTips
        }
    }

    fun getPatientFruitScoreByUserID(userID: String) {
        viewModelScope.launch {
            val score = patientRepository.getPatientFruitScoreByUserID(userID)
            // Optimal score is calculated as below
            if (score.fruitServeSize < 2 || score.fruitVariationScore < 2) {
                _fruitScoreOptimal.value = false
            } else {
                _fruitScoreOptimal.value = true
            }
        }
    }

    fun searchFruit(query: String) {
        _fruit.value = null
        viewModelScope.launch {
            _isFruitLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    fruitApi.getFruitByName(query.lowercase().trim())
                }
                _fruitName.value = query
                _fruit.value = result
                _errorMessage.value = ""
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Fruit Not Found. Please give a valid fruit name."
                }
            }
            _isFruitLoading.value = false
        }

    }

    fun clearError() {
        _errorMessage.value = ""
    }

    class NutriCoachScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NutriCoachScreenViewModel(context) as T
    }

}