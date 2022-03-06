package com.example.yeoassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yeoassignment.database.ContactEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ContactEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_TIMESTAMP)
    val timestamp: String
) {
    companion object {
        const val TABLE_NAME = "contacts"
        const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }
}