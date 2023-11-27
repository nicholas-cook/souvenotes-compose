package com.souvenotes.souvenotes.repository.prefs

import kotlinx.coroutines.flow.StateFlow

interface SouvenotesPrefs {

    companion object {
        internal const val PREFS_FILE_NAME = "com.souvenotes.souvenotes.souvenotes_prefs"
        internal const val KEY_APP_THEME_PREF = "APP_THEME_PREF"
    }

    val appThemePrefFlow: StateFlow<AppThemePref>

    fun setAppThemePref(appThemePref: AppThemePref)
}