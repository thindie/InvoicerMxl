package data.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun getDispatcher() : CoroutineDispatcher
}