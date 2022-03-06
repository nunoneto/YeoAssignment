package com.example.yeoassignment.repository

import com.example.yeoassignment.data.IContactsDataSource
import com.example.yeoassignment.data.NativeContact
import com.example.yeoassignment.database.*
import com.example.yeoassignment.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ContactsRepositoryImplTest {

    @Mock
    private lateinit var contactDataSource: IContactsDataSource

    @Mock
    private lateinit var contactFilter: ContactFilter

    @Mock
    private lateinit var yeoDatabase: YeoDatabase

    @Mock
    private lateinit var dateUtils: DateUtils

    @Mock
    private lateinit var contactDao: ContactDao

    @Mock
    private lateinit var phoneNumberDao: PhoneNumberDao

    private lateinit var underTest: IContactsRepository

    @Before
    fun setup() {
        underTest = ContactsRepositoryImpl(contactDataSource, contactFilter, yeoDatabase, dateUtils)

        given(yeoDatabase.contactDao()).willReturn(contactDao)
        given(yeoDatabase.phoneNumberDao()).willReturn(phoneNumberDao)

    }

    @Test
    fun `given new valid contact, should add to database`() = runTest {
        val newContact =
            NativeContact(id = "123", name = "Test", phoneNumbers = listOf("919999999"))
        given(contactDataSource.getAllContacts()).willReturn(listOf(newContact))
        given(contactFilter.filterValidContacts(listOf(newContact))).willReturn(listOf(newContact))

        given(contactDao.getAllContactsSync()).willReturn(emptyList())
        given(phoneNumberDao.getAllSync()).willReturn(emptyList())

        val now = Date()
        given(dateUtils.getNow()).willReturn(now)

        underTest.importContactsIntoDb()

        inOrder(contactDataSource, contactDao, phoneNumberDao, dateUtils, contactFilter).apply {
            verify(contactDataSource).getAllContacts()
            verify(contactFilter).filterValidContacts(listOf(newContact))
            verify(contactDao).getAllContactsSync()
            verify(phoneNumberDao).getAllSync()
            verify(dateUtils).getNow()
            verify(contactDao).deleteAll()
            verify(contactDao).insertContacts(
                listOf(
                    ContactEntity(
                        id = "123",
                        name = "Test",
                        timestamp = now.time.toString()
                    )
                )
            )

            verify(phoneNumberDao).deleteAll()
            verify(phoneNumberDao).insertAll(
                listOf(
                    PhoneNumberEntity(
                        number = "919999999",
                        contactId = "123"
                    )
                )
            )
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `given existing contact with different number, should update number and timestamp`() =
        runTest {
            val newContact =
                NativeContact(id = "123", name = "Test", phoneNumbers = listOf("919999999"))
            given(contactDataSource.getAllContacts()).willReturn(listOf(newContact))
            given(contactFilter.filterValidContacts(listOf(newContact))).willReturn(
                listOf(
                    newContact
                )
            )

            given(contactDao.getAllContactsSync()).willReturn(
                listOf(
                    ContactEntity(
                        id = "123",
                        name = "Test",
                        timestamp = "0"
                    )
                )
            )
            given(phoneNumberDao.getAllSync()).willReturn(
                listOf(
                    PhoneNumberEntity(
                        number = "919999991",
                        contactId = "123"
                    )
                )
            )

            val now = Date()
            given(dateUtils.getNow()).willReturn(now)

            underTest.importContactsIntoDb()

            inOrder(contactDataSource, contactDao, phoneNumberDao, dateUtils, contactFilter).apply {
                verify(contactDataSource).getAllContacts()
                verify(contactFilter).filterValidContacts(listOf(newContact))
                verify(contactDao).getAllContactsSync()
                verify(phoneNumberDao).getAllSync()
                verify(dateUtils).getNow()
                verify(contactDao).deleteAll()
                verify(contactDao).insertContacts(
                    listOf(
                        ContactEntity(
                            id = "123",
                            name = "Test",
                            timestamp = now.time.toString()
                        )
                    )
                )

                verify(phoneNumberDao).deleteAll()
                verify(phoneNumberDao).insertAll(
                    listOf(
                        PhoneNumberEntity(
                            number = "919999999",
                            contactId = "123"
                        )
                    )
                )
                verifyNoMoreInteractions()
            }
        }


    @Test
    fun `given existing contact without changes, shouldnt update timestamp`() =
        runTest {
            val newContact =
                NativeContact(id = "123", name = "Test", phoneNumbers = listOf("919999999"))
            given(contactDataSource.getAllContacts()).willReturn(listOf(newContact))
            given(contactFilter.filterValidContacts(listOf(newContact))).willReturn(
                listOf(
                    newContact
                )
            )

            given(contactDao.getAllContactsSync()).willReturn(
                listOf(
                    ContactEntity(
                        id = "123",
                        name = "Test",
                        timestamp = "11111111"
                    )
                )
            )
            given(phoneNumberDao.getAllSync()).willReturn(
                listOf(
                    PhoneNumberEntity(
                        number = "919999999",
                        contactId = "123"
                    )
                )
            )

            val now = Date()
            given(dateUtils.getNow()).willReturn(now)

            underTest.importContactsIntoDb()

            inOrder(contactDataSource, contactDao, phoneNumberDao, dateUtils, contactFilter).apply {
                verify(contactDataSource).getAllContacts()
                verify(contactFilter).filterValidContacts(listOf(newContact))
                verify(contactDao).getAllContactsSync()
                verify(phoneNumberDao).getAllSync()
                verify(dateUtils).getNow()
                verify(contactDao).deleteAll()
                verify(contactDao).insertContacts(
                    listOf(
                        ContactEntity(
                            id = "123",
                            name = "Test",
                            timestamp = "11111111"
                        )
                    )
                )

                verify(phoneNumberDao).deleteAll()
                verify(phoneNumberDao).insertAll(
                    listOf(
                        PhoneNumberEntity(
                            number = "919999999",
                            contactId = "123"
                        )
                    )
                )
                verifyNoMoreInteractions()
            }
        }


    @Test
    fun `given contact no longer exists, should delete it`() =
        runTest {
            given(contactDataSource.getAllContacts()).willReturn(emptyList())
            given(contactFilter.filterValidContacts(emptyList())).willReturn(emptyList())

            given(contactDao.getAllContactsSync()).willReturn(
                listOf(
                    ContactEntity(
                        id = "123",
                        name = "Test",
                        timestamp = "11111111"
                    )
                )
            )
            given(phoneNumberDao.getAllSync()).willReturn(
                listOf(
                    PhoneNumberEntity(
                        number = "919999999",
                        contactId = "123"
                    )
                )
            )

            val now = Date()
            given(dateUtils.getNow()).willReturn(now)

            underTest.importContactsIntoDb()

            inOrder(contactDataSource, contactDao, phoneNumberDao, dateUtils, contactFilter).apply {
                verify(contactDataSource).getAllContacts()
                verify(contactFilter).filterValidContacts(emptyList())
                verify(contactDao).getAllContactsSync()
                verify(phoneNumberDao).getAllSync()
                verify(dateUtils).getNow()
                verify(contactDao).deleteAll()
                verify(contactDao).insertContacts(emptyList())
                verify(phoneNumberDao).deleteAll()
                verify(phoneNumberDao).insertAll(emptyList())
                verifyNoMoreInteractions()
            }
        }
}