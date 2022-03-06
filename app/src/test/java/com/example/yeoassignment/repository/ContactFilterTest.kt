package com.example.yeoassignment.repository

import com.example.yeoassignment.data.NativeContact
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContactFilterTest : TestCase() {

    private lateinit var underTest: ContactFilter

    @Before
    fun setup() {
        underTest = ContactFilter()
    }

    @Test
    fun `given contact without phone numbers, should filter it out`() {
        val contact = contact()

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            emptyList<NativeContact>(),
            actual
        )
    }

    @Test
    fun `given contact with phone number containing invalid chars, should filter it out`() {
        val contact = contact().copy(
            phoneNumbers = listOf(
                "919999999r"
            )
        )

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            emptyList<NativeContact>(),
            actual
        )
    }

    @Test
    fun `given contact with phone number under min range, should filter it out`() {
        val contact = contact().copy(
            phoneNumbers = listOf(
                "99999999"
            )
        )

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            emptyList<NativeContact>(),
            actual
        )
    }

    @Test
    fun `given contact with phone number over max range, should filter it out`() {
        val contact = contact().copy(
            phoneNumbers = listOf(
                "9999999999999999"
            )
        )

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            emptyList<NativeContact>(),
            actual
        )
    }

    @Test
    fun `given contact with phone number with spaces, should remove spaces from number`() {
        val contact = contact().copy(
            phoneNumbers = listOf(
                "91 999 99 999"
            )
        )

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            listOf(
                contact().copy(
                    phoneNumbers = listOf(
                        "9199999999"
                    )
                )
            ),
            actual
        )
    }

    @Test
    fun `given contact with valid phone number without spaces, should preserve number`() {
        val contact = contact().copy(
            phoneNumbers = listOf(
                "919999999"
            )
        )

        val actual = underTest.filterValidContacts(listOf(contact))

        assertEquals(
            listOf(contact),
            actual
        )
    }

    private fun contact() = NativeContact(
        id = "123",
        name = "test",
        phoneNumbers = emptyList()
    )
}