package di

import dagger.Component
import ui.EntryPoint

@Component(modules = [FileViewSystemProviderModule::class, DispatcherProviderModule::class])

interface AppComponent {
    companion object{
        fun init(): AppComponent{
            return DaggerAppComponent
                .factory()
                .create()
        }
    }
    @Component.Factory
    interface Factory{
        fun create(): AppComponent
    }

    fun inject(entryPoint: EntryPoint)
}