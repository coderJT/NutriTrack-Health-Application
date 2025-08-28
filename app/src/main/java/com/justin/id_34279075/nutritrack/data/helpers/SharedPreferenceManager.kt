package com.justin.id_34279075.nutritrack.data.helpers

import android.content.Context
import android.content.SharedPreferences

/**
 * This will handle storing the userid and username within the shared preference so user would not have to login again.
 */
class SharedPreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    companion object {
        private const val USERID = "user_id"
        private const val USERNAME = "user_name"
    }

    fun login(userID: String, userName: String) {
        prefs.edit().apply {
            putString(USERID, userID)
            putString(USERNAME, userName)
        }.apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun getUserID(): String? {
        return prefs.getString(USERID, null)
    }

    fun getUserName(): String? {
        return prefs.getString(USERNAME, null)
    }
}