package com.example.yeoassignment.di

import android.content.Context
import com.example.yeoassignment.database.YeoDatabase
import com.example.yeoassignment.database.YeoDatabaseFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): YeoDatabase =
        YeoDatabaseFactory.build(context)
}