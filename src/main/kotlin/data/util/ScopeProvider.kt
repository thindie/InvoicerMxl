package data.util

import kotlinx.coroutines.CoroutineScope

interface ScopeProvider {
    fun getScope(): CoroutineScope
}