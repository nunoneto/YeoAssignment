package com.example.yeoassignment.database

import android.content.Context
import androidx.room.Room
import com.example.yeoassignment.database.YeoDatabase.Companion.DATABASE_NAME

object YeoDatabaseFactory {

    fun build(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        YeoDatabase::class.java, DATABASE_NAME
    ).build()
}