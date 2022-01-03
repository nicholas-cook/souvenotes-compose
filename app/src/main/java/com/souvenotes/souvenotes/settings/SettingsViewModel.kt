package com.souvenotes.souvenotes.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs

class SettingsViewModel(private val souvenotesPrefs: SouvenotesPrefs) : ViewModel() {

    var currentAppTheme by mutableStateOf(souvenotesPrefs.getAppThemePref())
        private set

    fun onAppThemeSelected(appThemePref: AppThemePref) {
        if (currentAppTheme != appThemePref) {
            souvenotesPrefs.setAppThemePref(appThemePref)
            currentAppTheme = appThemePref
        }
    }
}