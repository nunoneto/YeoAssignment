package com.example.yeoassignment.viewmodel

import com.example.yeoassignment.domain.ContactDomain
import com.example.yeoassignment.repository.IContactsRepository
import com.example.yeoassignment.util.CoroutineTestRule
import com.example.yeoassignment.util.UnitTestDispatcher
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ContactsViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var repo: IContactsRepository

    private lateinit var underTest: ContactsViewModel

    @Before
    fun setup() {
        underTest = ContactsViewModel(repo, UnitTestDispatcher)
    }

    @Test
    fun `given contacts are available, show return them`() = runTest {
        val contact = listOf(mock(ContactDomain::class.java))
        given(repo.observeContacts()).willReturn(flowOf(contact))

        val actual = mutableListOf<List<ContactDomain>>()
        underTest.contactsStateFlow.take(2).toList(actual)

        assertEquals(
            listOf(
                emptyList(),
                contact
            ),
            actual
        )
    }

    @Test
    fun `given user triggers import contacts, should invoke repo layer`() = runTest {
        launch {
            underTest.importContacts()
        }.join()

        verify(repo).importContactsIntoDb()
    }
}