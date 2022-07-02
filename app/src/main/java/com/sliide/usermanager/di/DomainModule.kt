package com.sliide.usermanager.di

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.UsersRepo
import com.sliide.usermanager.domain.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideUsersRepo(usersService: UsersService): UsersRepo {
        return UsersRepository(usersService)
    }
}