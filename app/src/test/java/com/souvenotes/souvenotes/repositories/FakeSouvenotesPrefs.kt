package com.souvenotes.souvenotes.repositories

import android.content.SharedPreferences
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs

class FakeSouvenotesPrefs : SouvenotesPrefs {

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null
    private var appThemePref = AppThemePref.System

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.listener = listener
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        this.listener = null
    }

    override fun getAppThemePref(): AppThemePref {
        return appThemePref
    }

    override fun setAppThemePref(appThemePref: AppThemePref) {
        this.appThemePref = appThemePref
    }
}