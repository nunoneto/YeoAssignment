package com.example.yeoassignment.utils

import kotlinx.coroutines.CoroutineDispatcher

interface IDispatcherProvider {
    fun background(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}