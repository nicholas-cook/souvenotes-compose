package com.souvenotes.souvenotes.repository.prefs.di

import android.content.Context
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    @Provides
    @Singleton
    fun provideSouvenotesPrefs(@ApplicationContext context: Context): SouvenotesPrefs {
        return SouvenotesPrefsImpl(context)
    }
}