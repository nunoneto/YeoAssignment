package com.example.yeoassignment.data

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ContactDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : IContactsDataSource {

    override fun getAllContacts(): List<NativeContact> {
        val contentResolver = context.contentResolver
        val contacts = mutableListOf<NativeContact>()
        contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            ?.run {
                if (count > 0) {
                    while (moveToNext()) {
                        val idIndex = getColumnIndex(ContactsContract.Contacts._ID)
                        val nameIndex = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val hasNumberIndex =
                            getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                        if (idIndex >= 0 && nameIndex >= 0 && hasNumberIndex >= 0) {
                            val id = getString(idIndex)
                            val name = getString(nameIndex)
                            val hasNumbers = getInt(hasNumberIndex)

                            if (!id.isNullOrBlank() && !name.isNullOrBlank() && hasNumbers > 0) {
                                getPhoneNumbersByContact(
                                    contentResolver,
                                    id
                                ).takeIf { it.isNotEmpty() }?.run {
                                    contacts.add(
                                        NativeContact(
                                            id = id,
                                            name = name,
                                            phoneNumbers = this
                                        )
                                    )

                                }
                            }
                        }
                    }
                }
                close()
            }

        return contacts
    }

    private fun getPhoneNumbersByContact(
        contentResolver: ContentResolver,
        id: String,
    ): MutableList<String> {
        val phoneNumbersCursor = contentResolver.query(
            Phone.CONTENT_URI,
            null,
            Phone.CONTACT_ID + " = ?", arrayOf(id), null
        )

        val phoneNumbers = mutableListOf<String>()
        while (phoneNumbersCursor?.moveToNext() == true) {
            phoneNumbersCursor.getColumnIndex(
                Phone.NUMBER
            ).takeIf { it >= 0 }?.let {
                phoneNumbers.add(phoneNumbersCursor.getString(it))
            }
        }
        phoneNumbersCursor?.close()
        return phoneNumbers
    }
}