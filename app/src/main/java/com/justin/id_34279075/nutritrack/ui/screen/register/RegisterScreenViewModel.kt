package com.justin.id_34279075.nutritrack.ui.screen.register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    var userID by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showExpandedUserIDs by mutableStateOf(false)
        private set

    var showPassword by mutableStateOf(false)
        private set

    var showPhoneNumber by mutableStateOf(false)
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    /**
     * Loads all existing user IDs
     */
    val userIDs: LiveData<List<String>> = patientRepository.getAllNotRegisteredPatientUserIDs()

    fun onUserIDChange(updatedUserID: String) {
        userID = updatedUserID
    }

    fun onPhoneNumberChange(updatedPhoneNumber: String) {
        this.phoneNumber = updatedPhoneNumber
    }

    fun onUserNameChange(updatedUserName: String) {
        this.userName = updatedUserName
    }

    fun onPasswordChange(updatedPassword: String) {
        this.password = updatedPassword
    }

    fun onConfirmPasswordChange(updatedConfirmPassword: String) {
        this.confirmPassword = updatedConfirmPassword
    }

    fun toggleShowExpandedUserIDs() {
        showExpandedUserIDs = !showExpandedUserIDs
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun toggleShowPhoneNumber() {
        showPhoneNumber = !showPhoneNumber
    }

    fun toggleShowConfirmPassword() {
        showConfirmPassword = !showConfirmPassword
    }

    /**
     * This function will verify and register the credentials provided (userID, phone number, userName,
     * password) and invokes the
     *
     * @param userID: userID of the patient.
     * @param phoneNumber: Phone number of the patient.
     */
    suspend fun register() {
        try {
            val patient = withContext(Dispatchers.IO) {
                patientRepository.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
            }

            if (patient.name == null) {
                withContext(Dispatchers.IO) {
                    patientRepository.updatePatientNameAndPassword(
                        userID, phoneNumber, userName, password
                    )
                }
                _errorMessage.value = ""
            } else {
                _errorMessage.value = "User already registered."
            }

        } catch (e: IllegalArgumentException) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.message
            }
        }
    }

    suspend fun canBeRegistered(): Boolean {
        return try {
            when {
                userID.isBlank() -> {
                    _errorMessage.value = "User ID cannot be empty. Please try again."
                    false
                }
                phoneNumber.isBlank() -> {
                    _errorMessage.value = "Phone Number cannot be empty. Please try again."
                    false
                }
                userName.isBlank() -> {
                    _errorMessage.value = "Username cannot be empty. Please try again."
                    false
                }
                password.isBlank() -> {
                    _errorMessage.value = "Password cannot be empty. Please try again."
                    false
                }
                confirmPassword.isBlank() -> {
                    _errorMessage.value = "Please confirm your password and try again."
                    false
                }
                password != confirmPassword -> {
                    _errorMessage.value = "Passwords do not match. Please make sure both passwords match and try again."
                    false
                }
                else -> {
                    _errorMessage.value = ""
                    val patient = withContext(Dispatchers.IO) {
                        patientRepository.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
                    }
                    true
                }
            }
        } catch (e: IllegalArgumentException) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.message
            }
            false
        }
    }


    fun clearError() {
        _errorMessage.value = ""
    }

    class RegisterScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterScreenViewModel(context) as T
    }

}