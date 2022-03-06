package com.example.yeoassignment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yeoassignment.domain.ContactDomain
import com.example.yeoassignment.repository.IContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactRepository: IContactsRepository
) : ViewModel() {

    val contactsStateFlow: StateFlow<List<ContactDomain>>
        get() = contactRepository.observeContacts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun importContacts() = viewModelScope.launch {
        Log.d("CENAS", "launch: ${Thread.currentThread()}")
        withContext(Dispatchers.IO) {
            Log.d("CENAS", "withContext: ${Thread.currentThread()}")
            kotlin.runCatching { contactRepository.importContactsIntoDb() }
                .onSuccess {
                    Log.d("CENAS", "onSuccess: ${Thread.currentThread()}")

                }
                .onFailure { }
        }
    }
}