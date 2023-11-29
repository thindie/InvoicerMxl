package data.util.implementations

import data.util.DispatcherProvider
import data.util.ScopeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DispatchersIOProvider @Inject constructor(): DispatcherProvider, ScopeProvider {
    override fun getDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun getScope(): CoroutineScope {
        return CoroutineScope(getDispatcher())
    }

}