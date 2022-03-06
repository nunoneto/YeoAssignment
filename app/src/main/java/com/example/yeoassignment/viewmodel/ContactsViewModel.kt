package com.example.yeoassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yeoassignment.domain.ContactDomain
import com.example.yeoassignment.repository.IContactsRepository
import com.example.yeoassignment.utils.IDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactRepository: IContactsRepository,
    private val dispatcherProvider: IDispatcherProvider
) : ViewModel() {

    val contactsStateFlow: StateFlow<List<ContactDomain>>
        get() = contactRepository.observeContacts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun importContacts() = viewModelScope.launch {
        withContext(dispatcherProvider.background()) {
            kotlin.runCatching { contactRepository.importContactsIntoDb() }
        }
    }
}