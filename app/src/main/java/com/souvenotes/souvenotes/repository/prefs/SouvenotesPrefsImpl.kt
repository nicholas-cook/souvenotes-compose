package com.souvenotes.souvenotes.repository.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SouvenotesPrefsImpl(context: Context) : SouvenotesPrefs {

    private var sharedPreferences: SharedPreferences

    init {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        sharedPreferences = EncryptedSharedPreferences.create(
            SouvenotesPrefs.PREFS_FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun getAppThemePref(): AppThemePref {
        val name = sharedPreferences.getString(SouvenotesPrefs.KEY_APP_THEME_PREF, null)
            ?: AppThemePref.System.name
        return AppThemePref.valueOf(name)
    }

    override fun setAppThemePref(appThemePref: AppThemePref) {
        sharedPreferences.edit { putString(SouvenotesPrefs.KEY_APP_THEME_PREF, appThemePref.name) }
    }
}