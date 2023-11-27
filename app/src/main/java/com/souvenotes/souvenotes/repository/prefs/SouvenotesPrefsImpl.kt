package com.souvenotes.souvenotes.repository.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SouvenotesPrefsImpl(context: Context) : SouvenotesPrefs {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SouvenotesPrefs.PREFS_FILE_NAME, Context.MODE_PRIVATE)

    private val _appThemePrefFlow: MutableStateFlow<AppThemePref>

    init {
        val appThemePref = AppThemePref.valueOf(
            sharedPreferences.getString(
                SouvenotesPrefs.KEY_APP_THEME_PREF,
                AppThemePref.System.name
            ) ?: AppThemePref.System.name
        )
        _appThemePrefFlow = MutableStateFlow(appThemePref)
    }

    override val appThemePrefFlow: StateFlow<AppThemePref> = _appThemePrefFlow

    override fun setAppThemePref(appThemePref: AppThemePref) {
        sharedPreferences.edit {
            putString(SouvenotesPrefs.KEY_APP_THEME_PREF, appThemePref.name)
        }
        _appThemePrefFlow.value = appThemePref
    }
}