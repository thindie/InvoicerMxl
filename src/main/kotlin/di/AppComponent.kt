package di

import dagger.Component
import root.InvoicerApplication
import ui.EntryPoint
import javax.inject.Singleton

@Component(modules = [FileViewSystemProviderModule::class, DispatcherProviderModule::class, RepositoriesModule::class, PathProviderModule::class])
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

    fun inject(application: InvoicerApplication)
}