package com.example.yeoassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.yeoassignment.database.ContactEntity.Companion.TABLE_NAME
import com.example.yeoassignment.database.PhoneNumberEntity.Companion.COLUMN_CONTACT_ID

@Entity(
    tableName = PhoneNumberEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = [ContactEntity.COLUMN_ID],
            childColumns = [COLUMN_CONTACT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhoneNumberEntity(
    @ColumnInfo(name = COLUMN_NUMBER)
    val number: String,
    @ColumnInfo(name = COLUMN_CONTACT_ID)
    val contactId: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "phone_number"
        private const val COLUMN_NUMBER = "number"
        const val COLUMN_CONTACT_ID = "contact_id_fk"
    }
}