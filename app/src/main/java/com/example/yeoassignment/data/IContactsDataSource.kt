package com.example.yeoassignment.data

interface IContactsDataSource {
    fun getAllContacts(): List<NativeContact>
}