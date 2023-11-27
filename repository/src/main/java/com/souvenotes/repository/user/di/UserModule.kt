package com.souvenotes.repository.user.di

import com.souvenotes.repository.user.UserRepository
import com.souvenotes.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UserModule {

    @Binds
    @Singleton
    fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}