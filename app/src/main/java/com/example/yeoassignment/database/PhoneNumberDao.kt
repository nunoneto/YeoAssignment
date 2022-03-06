package com.example.yeoassignment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PhoneNumberDao {

    @Insert(onConflict = REPLACE)
    abstract fun insertAll(numbers: List<PhoneNumberEntity>)

    @Query(QUERY_GET_ALL)
    abstract fun getAll(): Flow<List<PhoneNumberEntity>>

    @Query(QUERY_GET_ALL)
    abstract suspend fun getAllSync(): List<PhoneNumberEntity>

    @Query(DELETE_ALL)
    abstract fun deleteAll()

    companion object {
        private const val QUERY_GET_ALL = "SELECT * FROM ${PhoneNumberEntity.TABLE_NAME}"
        private const val DELETE_ALL = "DELETE FROM ${PhoneNumberEntity.TABLE_NAME}"
    }
}