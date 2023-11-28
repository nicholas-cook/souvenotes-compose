package com.souvenotes.souvenotes.repositories

import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSouvenotesPrefs : SouvenotesPrefs {

    override val appThemePrefFlow: StateFlow<AppThemePref> = MutableStateFlow(AppThemePref.System)

    override fun setAppThemePref(appThemePref: AppThemePref) {
        (appThemePrefFlow as MutableStateFlow).value = appThemePref
    }
}