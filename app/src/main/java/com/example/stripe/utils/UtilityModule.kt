package com.example.stripe.utils

import com.example.stripe.utils.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {
    @Provides
    @Singleton
    fun provideUtility(): Utility {
        return Utility()
    }
}