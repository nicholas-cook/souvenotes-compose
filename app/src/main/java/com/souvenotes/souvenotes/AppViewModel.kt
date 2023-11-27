package com.souvenotes.souvenotes

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
class AppViewModel @Inject constructor(private val souvenotesPrefs: SouvenotesPrefs) : ViewModel() {

    private val _appTheme = MutableStateFlow(souvenotesPrefs.appThemePrefFlow.value)
    val appTheme: StateFlow<AppThemePref> = _appTheme.asStateFlow()

    init {
        viewModelScope.launch {
            souvenotesPrefs.appThemePrefFlow.collect {
                _appTheme.value = it
            }
        }
    }
}