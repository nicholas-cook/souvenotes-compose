package com.souvenotes.souvenotes.repository.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SouvenotesPrefsImpl(context: Context) : SouvenotesPrefs {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SouvenotesPrefs.PREFS_FILE_NAME, Context.MODE_PRIVATE)

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override var appThemePref: AppThemePref
        get() {
            val name = sharedPreferences.getString(SouvenotesPrefs.KEY_APP_THEME_PREF, null)
                ?: AppThemePref.System.name
            return AppThemePref.valueOf(name)
        }
        set(value) = sharedPreferences.edit {
            putString(
                SouvenotesPrefs.KEY_APP_THEME_PREF,
                value.name
            )
        }
}