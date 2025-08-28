package com.justin.id_34279075.nutritrack.ui.screen.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.patient.PatientBasicInfo
import com.justin.id_34279075.nutritrack.data.patient.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(context: Context): ViewModel() {

    private val patientRepository: PatientRepository = PatientRepository(context)

    private val _userBasicInfo = MutableLiveData<PatientBasicInfo>()
    val userBasicInfo: LiveData<PatientBasicInfo> = _userBasicInfo

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        val userID = AuthManager.getCurrentUserID()
        if (userID != null) {
            getPatientBasicInfo(userID)
        }
    }

    fun getPatientBasicInfo(userID: String) {

        viewModelScope.launch {
            try {
                val info = withContext(Dispatchers.IO) {
                    patientRepository.getPatientBasicInfoByUserID(userID)
                }
                _userBasicInfo.value = info

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                }
            }
        }
    }

    class SettingsScreenViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsScreenViewModel(context) as T
    }

}