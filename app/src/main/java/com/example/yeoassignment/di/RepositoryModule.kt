package com.example.yeoassignment.di

import com.example.yeoassignment.repository.ContactsRepositoryImpl
import com.example.yeoassignment.repository.IContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun providesContactsRepository(
        contactsRepositoryImpl: ContactsRepositoryImpl
    ): IContactsRepository
}