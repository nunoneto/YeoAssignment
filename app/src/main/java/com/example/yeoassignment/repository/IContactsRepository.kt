package com.example.yeoassignment.repository

import com.example.yeoassignment.domain.ContactDomain
import kotlinx.coroutines.flow.Flow

interface IContactsRepository {
    suspend fun importContactsIntoDb()
    fun observeContacts(): Flow<List<ContactDomain>>
}