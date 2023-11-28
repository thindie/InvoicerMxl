package di

import dagger.Binds
import dagger.Module
import data.util.DispatcherProvider
import data.util.DispatchersIOProvider
import data.util.ScopeProvider

@Module
interface DispatcherProviderModule {

    @Binds
    fun bindDispatcherProvider(impl: DispatchersIOProvider): DispatcherProvider

    @Binds
    fun bindsScopeProvider(impl: DispatchersIOProvider): ScopeProvider
}