package com.souvenotes.souvenotes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val souvenotesPrefs: SouvenotesPrefs) :
    ViewModel() {

    private val _currentAppTheme = MutableStateFlow(souvenotesPrefs.appThemePrefFlow.value)
    val currentAppTheme: StateFlow<AppThemePref> = _currentAppTheme.asStateFlow()

    init {
        viewModelScope.launch {
            souvenotesPrefs.appThemePrefFlow.collect {
                _currentAppTheme.value = it
            }
        }
    }

    fun onAppThemeSelected(appThemePref: AppThemePref) {
        if (currentAppTheme.value != appThemePref) {
            souvenotesPrefs.setAppThemePref(appThemePref)
        }
    }
}