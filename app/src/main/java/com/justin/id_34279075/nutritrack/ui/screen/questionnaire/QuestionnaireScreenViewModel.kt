package com.justin.id_34279075.nutritrack.ui.screen.questionnaire

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
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategory
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodIntake
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodIntakeRepository
import com.justin.id_34279075.nutritrack.data.foodIntake.Persona
import com.justin.id_34279075.nutritrack.data.foodIntake.TimingQuestion
import com.justin.id_34279075.nutritrack.data.foodIntake.TimingQuestionWithAnswer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class QuestionnaireScreenViewModel(context: Context): ViewModel() {

    private val foodIntakeRepository: FoodIntakeRepository = FoodIntakeRepository(context)

    private val _selectedPersona = MutableLiveData<Persona?>(null)
    val selectedPersona: LiveData<Persona?> get() = _selectedPersona

    private val _selectedFoodCategories = MutableLiveData<List<FoodCategory>?>()
    val selectedFoodCategories: LiveData<List<FoodCategory>?> get() = _selectedFoodCategories

    private val _selectedTimings = MutableLiveData<List<TimingQuestionWithAnswer>?>(
        TimingQuestion.entries.map { TimingQuestionWithAnswer(it, "") }
    )
    val selectedTimings: MutableLiveData<List<TimingQuestionWithAnswer>?> = _selectedTimings

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoadingQuestionnaireValues = MutableLiveData<Boolean>(false)
    val isLoadingQuestionnaireValues: LiveData<Boolean> get() = _isLoadingQuestionnaireValues

    var showDialog = mutableStateOf<Persona?>(null)
    var expandedDropdownMenu = mutableStateOf(false)

    var showTimePickerForQuestion by mutableStateOf<TimingQuestion?>(null)
        private set

    fun setShowTimePickerQuestion(question: TimingQuestion?) {
        showTimePickerForQuestion = question
    }

    init {
        val userID = AuthManager.getCurrentUserID()
        if (userID != null) {
            loadPatientFoodIntake(userID)
        }
    }

    /**
     * Handle the toggling of timing questions.
     *
     * @param question TimingQuestion entity that is toggled.
     * @param time LocalTime value to be set.
     */
    fun toggleTimingSelection(question: TimingQuestion, time: LocalTime) {

        _selectedTimings.value = _selectedTimings.value?.map {
            if (it.question == question) it.copy(answer = time.toString()) else it
        }
    }

    /**
     * Handle the toggling of persona.
     *
     * @param persona Persona enum that is toggled.
     */
    fun togglePersonaSelection(persona: Persona) {

        _selectedPersona.value = persona
    }

    /**
     * Handle the toggling of food categories.
     *
     * @param foodCategory FoodCategory enum that is toggled.
     */
    fun toggleFoodCategorySelection(foodCategory: FoodCategory) {

        val current = _selectedFoodCategories.value ?: emptyList()
        _selectedFoodCategories.value =
            if (current.contains(foodCategory)) {
                current - foodCategory
            } else {
                current + foodCategory
            }
    }

    /**
     * This function preloads previous questionnaire result into the current questionnaire.
     *
     * @param patientID: patientID value of the FoodIntake entity.
     */
    fun loadPatientFoodIntake(patientID: String) {
        _isLoadingQuestionnaireValues.value = true
        viewModelScope.launch {
            try {
                val foodIntake = withContext(Dispatchers.IO) {
                    foodIntakeRepository.getFoodIntakeByPatientID(patientID)
                }

                foodIntake.let {
                    _selectedPersona.value = it.persona
                    _selectedFoodCategories.value = it.foodCategories
                    _selectedTimings.value = it.timingQuestions
                }
            } catch (e: IllegalArgumentException) {
                // Acceptable if no FoodIntake found during initialization
            } finally {
                _isLoadingQuestionnaireValues.value = false
            }
        }
    }


    /**
     * Updates an existing FoodIntake entity through a given patientID or creates a new FoodIntake
     * entity if not found.
     *
     * @param patientID: patientID value of the target FoodIntake entity.
    */
    fun storePatientFoodIntake(patientID: String) {

        if (_selectedFoodCategories.value.isNullOrEmpty() ||
            _selectedPersona.value == null ||
            selectedTimings.value?.any { it.answer.isEmpty() || it.answer == "Select Time" } == true) {

            _errorMessage.value = "Fill in all necessary details. Ensure that at least one food category " +
                    "is selected, persona is selected, and that all timing questions are answered."
        } else {
            val foodIntake = FoodIntake(
                patientID = patientID,
                foodCategories = _selectedFoodCategories.value,
                persona = _selectedPersona.value,
                timingQuestions = _selectedTimings.value
            )

            viewModelScope.launch {
                try {
                    val existing = withContext(Dispatchers.IO) {
                        foodIntakeRepository.getFoodIntakeByPatientID(patientID)
                    }
                    foodIntakeRepository.update(foodIntake)
                } catch (e: IllegalArgumentException) {
                    foodIntakeRepository.insert(foodIntake)
                }
            }
        }
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    class QuestionnaireScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuestionnaireScreenViewModel(context) as T
    }


}