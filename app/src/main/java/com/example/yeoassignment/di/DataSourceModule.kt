package com.example.yeoassignment.di

import com.example.yeoassignment.data.ContactDataSourceImpl
import com.example.yeoassignment.data.IContactsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun provideContactsDataSource(
        contactsDataSourceModule: ContactDataSourceImpl
    ): IContactsDataSource
}