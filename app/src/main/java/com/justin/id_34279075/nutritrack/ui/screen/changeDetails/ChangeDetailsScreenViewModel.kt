package com.justin.id_34279075.nutritrack.ui.screen.changeDetails

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangeDetailsScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String> get() = _errorMessage

    private val userID = AuthManager.getCurrentUserID()

    var userName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var currentPassword by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var showCurrentPassword by mutableStateOf(false)
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    fun onUserNameChange(updatedUserName: String) {
        userName = updatedUserName
    }

    fun onPasswordChange(updatedPassword: String) {
        password = updatedPassword
    }

    fun onCurrentPasswordChange(updatedPassword: String) {
        currentPassword = updatedPassword
    }

    fun onConfirmPasswordChange(updatedPassword: String) {
        confirmPassword = updatedPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun toggleShowCurrentPassword() {
        showCurrentPassword = !showCurrentPassword
    }

    fun toggleShowConfirmPassword() {
        showConfirmPassword = !showConfirmPassword
    }

    init {
        AuthManager.getCurrentUserName()?.let { onUserNameChange(it) }
    }

    /**
     * This function will verify and register the credentials provided (userID, phone number, userName,
     * password) and invokes the
     */
    suspend fun updateDetails() {
        try {
            val patient = withContext(Dispatchers.IO) {
                patientRepository.getPatientByUserIDAndPassword(userID.toString(),currentPassword)
            }
                withContext(Dispatchers.IO) {
                    patientRepository.updatePatientNameAndPassword(
                        userID.toString(), patient.phoneNumber, userName, password
                    )
            }

        } catch (e: IllegalArgumentException) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = e.message
            }
        }
    }

    /**
     * Check whether user provided are valid for update.
     */
    suspend fun canBeUpdated(): Boolean {
        return try {
            when {
                userName.isBlank() -> {
                    _errorMessage.value = "Username cannot be empty. Please try again."
                    false
                }
                currentPassword.isBlank() -> {
                    _errorMessage.value = "Current password cannot be empty. Please try again."
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
     * Clears the error message value.
     */
    fun clearError() {
        _errorMessage.value = ""
    }

    class ChangeDetailsScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChangeDetailsScreenViewModel(context) as T
    }

}