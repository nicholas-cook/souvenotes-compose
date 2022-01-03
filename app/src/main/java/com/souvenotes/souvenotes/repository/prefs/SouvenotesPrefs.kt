package com.souvenotes.souvenotes.repository.prefs

import android.content.SharedPreferences

interface SouvenotesPrefs {

    companion object {
        internal const val PREFS_FILE_NAME = "souvenotes_prefs"
        internal const val KEY_APP_THEME_PREF = "APP_THEME_PREF"
    }

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun getAppThemePref(): AppThemePref

    fun setAppThemePref(appThemePref: AppThemePref)
}