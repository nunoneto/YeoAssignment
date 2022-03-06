package com.example.yeoassignment.di

import com.example.yeoassignment.utils.CoroutineDispatcher
import com.example.yeoassignment.utils.IDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DispatcherModule {

    @Binds
    fun providesDispatcher(dispatcher: CoroutineDispatcher): IDispatcherProvider
}