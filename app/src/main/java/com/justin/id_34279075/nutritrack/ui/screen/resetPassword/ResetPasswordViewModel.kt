package com.justin.id_34279075.nutritrack.ui.screen.resetPassword

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import com.justin.id_34279075.nutritrack.ui.screen.changeDetails.ChangeDetailsScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetPasswordViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String> get() = _errorMessage

    val userIDs: LiveData<List<String>> = patientRepository.getAllRegisteredPatientUserIDs()

    var showExpandedUserIDs by mutableStateOf(false)
        private set

    var userID by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showPhoneNumber by mutableStateOf(false)
        private set

    var showPassword by mutableStateOf(false)
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    fun onUserIDChange(updatedUserName: String) {
        userID = updatedUserName
    }

    fun onPhoneNumberChange(updatedPhoneNumber: String) {
        phoneNumber = updatedPhoneNumber
    }

    fun onPasswordChange(updatedPassword: String) {
        password = updatedPassword
    }

    fun onConfirmPasswordChange(updatedPassword: String) {
        confirmPassword = updatedPassword
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

    fun toggleShowExpandedUserIDs() {
        showExpandedUserIDs = !showExpandedUserIDs
    }


    init {
        AuthManager.getCurrentUserName()?.let { onUserIDChange(it) }
    }

    /**
     * Updates the user password given the user's id and phone number.
     */
    suspend fun updatePassword() {
        try {
            val patient = withContext(Dispatchers.IO) {
                patientRepository.getPatientByUserIDAndPhoneNumber(userID, phoneNumber)
            }
            withContext(Dispatchers.IO) {
                patientRepository.updatePatientPassword(
                    userID, patient.phoneNumber, password
                )
            }

        } catch (e: IllegalArgumentException) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.message
            }
        }
    }

    /**
     * Determines whether user input is valid and account can be reset next.
     */
    suspend fun canBeReset(): Boolean {
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

    /**
     * Clears the error value.
     */
    fun clearError() {
        _errorMessage.value = ""
    }

    /**
     * Factory for view model.
     */
    class ResetPasswordViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ResetPasswordViewModel(context) as T
    }

}