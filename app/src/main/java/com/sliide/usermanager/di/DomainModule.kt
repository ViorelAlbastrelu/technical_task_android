package com.sliide.usermanager.di

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.UsersRepo
import com.sliide.usermanager.domain.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    @ViewModelScoped
    fun provideUsersRepo(usersService: UsersService): UsersRepo {
        return UsersRepository(usersService)
    }
}