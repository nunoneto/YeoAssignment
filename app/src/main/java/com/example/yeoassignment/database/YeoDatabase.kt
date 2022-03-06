package com.example.yeoassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactEntity::class, PhoneNumberEntity::class], version = 1)
abstract class YeoDatabase: RoomDatabase() {
    abstract fun contactDao() : ContactDao
    abstract fun phoneNumberDao() : PhoneNumberDao

    companion object {
        const val DATABASE_NAME = "yeo-database"
    }
}