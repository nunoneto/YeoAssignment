package com.example.yeoassignment.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContacts(contacts: List<ContactEntity>)

    @Query(QUERY_GET_ALL)
    abstract suspend fun getAllContactsSync(): List<ContactEntity>

    @Query(QUERY_GET_ALL)
    abstract fun getAllContacts(): Flow<List<ContactEntity>>

    @Query(DELETE_ALL)
    abstract fun deleteAll()

    companion object {
        private const val QUERY_GET_ALL = "SELECT * FROM  ${ContactEntity.TABLE_NAME} "
        private const val DELETE_ALL = "DELETE FROM  ${ContactEntity.TABLE_NAME} "
    }
}