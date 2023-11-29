package di

import dagger.Component
import ui.EntryPoint
import javax.inject.Singleton

@Component(modules = [FileViewSystemProviderModule::class, DispatcherProviderModule::class, ActionsRepositoryModule::class, PathProviderModule::class])
@Singleton
interface AppComponent {
    companion object {
        fun init(): AppComponent {
            return DaggerAppComponent
                .factory()
                .create()
        }
    }

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun inject(entryPoint: EntryPoint)
}