package com.souvenotes.souvenotes.settings

import com.souvenotes.souvenotes.MainDispatcherRule
import com.souvenotes.souvenotes.repositories.FakeSouvenotesPrefs
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val souvenotesPrefs = FakeSouvenotesPrefs()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(souvenotesPrefs)
    }

    @Test
    fun `Test changing app theme is successful`() = runTest {
        viewModel.onAppThemeSelected(AppThemePref.Dark)
        val updatedAppTheme = viewModel.currentAppTheme.first()
        Assert.assertEquals(AppThemePref.Dark, updatedAppTheme)
    }
}