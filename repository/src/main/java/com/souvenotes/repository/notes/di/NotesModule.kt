package com.souvenotes.repository.notes.di

import com.souvenotes.repository.notes.NotesRepository
import com.souvenotes.repository.notes.NotesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotesModule {

    @Binds
    @Singleton
    fun bindsNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository
}