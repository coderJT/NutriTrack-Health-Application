package com.justin.id_34279075.nutritrack.ui.screen.login

import android.content.Context
import android.util.Log
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

/**
 * ViewModel class for the login screen.
 * This class connects the repository to perform database related operations.
 *
 * @param context: Application context.
 */
class LoginScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    var userID by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showExpandedUserIDs by mutableStateOf(false)
        private set

    var showPassword by mutableStateOf(false)
        private set

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Loads all existing user IDs
     */
    val userIDs: LiveData<List<String>> = patientRepository.getAllRegisteredPatientUserIDs()

    /**
     * This function will verify the credentials provided (userID, password) and invokes the
     * AuthManager to handle the login globally.
     */
    suspend fun login(): Boolean {
        return try {
            Log.d("userID", userID)
            if (userID.isBlank()) {
                _errorMessage.value = "User ID must be selected. Please try again."
                false
            } else if (password.isBlank()) {
                _errorMessage.value = "Password cannot be empty. Please try again."
                false
            } else {
                val patient = withContext(Dispatchers.IO) {
                    patientRepository.getPatientByUserIDAndPassword(userID, password)
                }
                patient.name?.let { AuthManager.login(userID, it) }
                true
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

    fun onUserIDChange(updatedUserID: String) {
        userID = updatedUserID
    }

    fun onPasswordChange(updatedPassword: String) {
        password = updatedPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun toggleShowExpandedUserIDs() {
        showExpandedUserIDs = !showExpandedUserIDs
    }

    class LoginScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LoginScreenViewModel(context) as T
    }


}