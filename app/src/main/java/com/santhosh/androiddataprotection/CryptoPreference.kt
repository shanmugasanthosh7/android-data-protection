package com.santhosh.androiddataprotection

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

/**
 * Created by Santhosh on 7/7/2020.
 */
class CryptoPreference(context: Context) {

    companion object {
        private const val PREFERENCE_FILE = "protected_preference"
    }

    var preferences: SharedPreferences


    init {
        preferences = EncryptedSharedPreferences.create(
            context,
            PREFERENCE_FILE,
            CryptoFileSystem.masterKeyAlias(context), // I used master key from CryptoFileSystem class maybe you can move it to some other common class.
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // preference key encryption scheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // preference value encryption scheme
        )
    }

    fun getPreferenceEdit(): SharedPreferences.Editor =
        preferences.edit() // return shared preference edit

}