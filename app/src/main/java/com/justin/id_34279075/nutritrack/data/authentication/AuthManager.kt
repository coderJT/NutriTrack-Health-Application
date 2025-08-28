package com.justin.id_34279075.nutritrack.data.authentication

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.justin.id_34279075.nutritrack.data.helpers.SharedPreferenceManager

/**
 * Handles the authentication part of the application on a global scale, specifically userID and username.
 * Manages a SharedPreferenceManager instance to persist user login sessions even after the app
 * activity is destroyed.
 */
object AuthManager {

    /**
     * userID and username are set to private to keep them inaccessible from outside code
     */
    private val _userID: MutableState<String?> = mutableStateOf(null)
    private val _username: MutableState<String?> = mutableStateOf(null)

    /**
     * Updates the sharedPreference settings to persist user login state.
     */
    private lateinit var sharedPrefManager: SharedPreferenceManager

    /**
     * Creates a SharedPreferenceManager instance on initialisation then load previous stored userID
     * and username (if available).
     *
     * @param context: Context.
     */
    fun init(context: Context) {
        sharedPrefManager = SharedPreferenceManager(context)

        _userID.value = sharedPrefManager.getUserID()
        _username.value = sharedPrefManager.getUserName()
    }

    /**
     * Logs in a patient for the current session, also updates the sharedPreference values.
     *
     * @param userID: UserID of the patient.
     * @param userName: Username of the patient.
     */
    fun login(userID: String, userName: String) {
        _userID.value = userID
        _username.value = userName
        sharedPrefManager.login(userID, userName)
    }

    /**
     * Logs out the patient of the current session, also clears the sharedPreference values.
     */
    fun logout() {
        _userID.value = null
        _username.value = null
        sharedPrefManager.logout()
    }

    /**
     * Retrieves the current patient's userID.
     *
     * @return [String] value of current patient's userID, or null if no logged in user.
     */
    fun getCurrentUserID(): String? {
        return _userID.value
    }

    /**
     * Retrieves the current patient's username.
     *
     * @return [String] value of current patient's username, or null if no logged in user.
     */
    fun getCurrentUserName(): String? {
        return _username.value
    }
}