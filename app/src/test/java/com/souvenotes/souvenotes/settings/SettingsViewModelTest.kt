package com.souvenotes.souvenotes.settings

import com.souvenotes.souvenotes.repositories.FakeSouvenotesPrefs
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private val souvenotesPrefs = FakeSouvenotesPrefs()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(souvenotesPrefs)
    }

    @Test
    fun `Test changing app theme is successful`() {
        viewModel.onAppThemeSelected(AppThemePref.Dark)
        Assert.assertEquals(AppThemePref.Dark, viewModel.currentAppTheme)
        Assert.assertEquals(AppThemePref.Dark, souvenotesPrefs.getAppThemePref())
    }
}