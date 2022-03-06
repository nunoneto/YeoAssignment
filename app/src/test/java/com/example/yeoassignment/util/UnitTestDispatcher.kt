package com.example.yeoassignment.util

import com.example.yeoassignment.utils.IDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
object UnitTestDispatcher : IDispatcherProvider {

    private val testDispatcher = UnconfinedTestDispatcher()

    override fun background(): CoroutineDispatcher = testDispatcher
    override fun main(): CoroutineDispatcher = testDispatcher
    override fun default() = testDispatcher
}