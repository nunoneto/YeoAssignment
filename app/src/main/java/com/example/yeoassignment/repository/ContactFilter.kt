package com.example.yeoassignment.repository

import com.example.yeoassignment.data.NativeContact
import javax.inject.Inject

class ContactFilter @Inject constructor() {

    /**
    Validates the phone numbers
     * Remove any spaces
     * Check the length is between 9 and 15 characters
     * Only contains numbers and the following characters: + ( ) . . -

    Returns only contacts that fulfill the above criteria
     */
    fun filterValidContacts(nativeContacts: List<NativeContact>) = nativeContacts.map {
        it.copy(phoneNumbers = filterPhoneNumbers(it.phoneNumbers))
    }.filter { it.phoneNumbers.isNotEmpty() }

    private fun filterPhoneNumbers(phoneNumbers: List<String>) = phoneNumbers
        .map { it.replace(WHITESPACE_REGEX, "") }
        .filter {
            it.matches(Regex(PHONE_NUMBER_REGEX))
        }

    companion object {
        private const val WHITESPACE_REGEX = "\\s+"
        private const val PHONE_NUMBER_REGEX = "^[0-9+().-]{9,15}\$"
    }
}