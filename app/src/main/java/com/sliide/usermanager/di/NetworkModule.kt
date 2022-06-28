package com.sliide.usermanager.di

import android.content.Context
import com.sliide.usermanager.R
import com.sliide.usermanager.api.RetrofitFactory
import com.sliide.usermanager.api.UsersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideUserService(
        @ApplicationContext context: Context
    ): UsersService {
        val baseUrl = context.getString(R.string.base_url)
        val token = context.getString(R.string.token)
        return RetrofitFactory(baseUrl, token).getInstance().create(UsersService::class.java)
    }
}