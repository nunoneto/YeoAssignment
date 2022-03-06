package com.example.yeoassignment.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class CoroutineTestRule(override val coroutineContext: CoroutineContext = StandardTestDispatcher()) :
    ExternalResource(), CoroutineScope {

    override fun before() {
        super.before()
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun after() {
        super.after()
        Dispatchers.resetMain()
    }
}