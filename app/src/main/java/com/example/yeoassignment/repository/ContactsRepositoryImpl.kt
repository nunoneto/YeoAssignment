package com.example.yeoassignment.repository

import com.example.yeoassignment.data.IContactsDataSource
import com.example.yeoassignment.data.NativeContact
import com.example.yeoassignment.database.ContactEntity
import com.example.yeoassignment.database.PhoneNumberEntity
import com.example.yeoassignment.database.YeoDatabase
import com.example.yeoassignment.domain.ContactDomain
import com.example.yeoassignment.utils.DateUtils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val contactDataSource: IContactsDataSource,
    private val contactFilter: ContactFilter,
    private val yeoDatabase: YeoDatabase,
    private val dateUtils: DateUtils
) : IContactsRepository {

    override suspend fun importContactsIntoDb() {
        val importedContacts = contactFilter.filterValidContacts(contactDataSource.getAllContacts())

        val currentContacts = yeoDatabase.contactDao().getAllContactsSync()
        val currentNumbers = yeoDatabase.phoneNumberDao().getAllSync()

        val timestamp = dateUtils.getNow().time.toString()

        val contactsToInsert =
            updatedContacts(currentContacts, importedContacts, currentNumbers, timestamp) +
                    newContacts(importedContacts, currentContacts, timestamp)

        yeoDatabase.contactDao().apply {
            deleteAll()
            insertContacts(contacts = contactsToInsert.sortedBy { it.name })
        }

        yeoDatabase.phoneNumberDao().apply {
            deleteAll()
            insertAll(getNumbers(importedContacts))
        }
    }

    @OptIn(FlowPreview::class)
    override fun observeContacts(): Flow<List<ContactDomain>> =
        yeoDatabase.contactDao().getAllContacts()
            .combine(yeoDatabase.phoneNumberDao().getAll()) { contacts, numbers ->
                contacts.map { contact ->
                    ContactDomain(
                        name = contact.name,
                        timestamp = contact.timestamp,
                        phoneNumbers = numbers.filter { it.contactId == contact.id }
                            .map { it.number })
                }
            }


    private fun getNumbers(importedContacts: List<NativeContact>) =
        importedContacts.flatMap { contact ->
            contact.phoneNumbers.map {
                PhoneNumberEntity(
                    contactId = contact.id,
                    number = it
                )
            }
        }

    private fun updatedContacts(
        currentContacts: List<ContactEntity>,
        filteredContacts: List<NativeContact>,
        currentNumbers: List<PhoneNumberEntity>,
        timestamp: String
    ) = currentContacts.mapNotNull { currentContact ->
        filteredContacts.firstOrNull { it.id == currentContact.id }?.run {
            if (name != currentContact.name || currentNumbers.filter { it.contactId == currentContact.id }
                    .map { it.number } != phoneNumbers) {
                ContactEntity(id = id, name = name, timestamp = timestamp)
            } else {
                currentContact
            }
        }
    }

    private fun newContacts(
        filteredContacts: List<NativeContact>,
        currentContacts: List<ContactEntity>,
        timestamp: String
    ) =
        filteredContacts.filter { importedContact ->
            currentContacts.none { it.id == importedContact.id }
        }.map { nativeContact ->
                ContactEntity(
                    id = nativeContact.id,
                    name = nativeContact.name,
                    timestamp = timestamp
                )
            }
}