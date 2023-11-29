package di

import dagger.Module
import dagger.Provides
import data.util.SystemPropertyPathProvider
import data.util.implementations.PathProvider
import javax.inject.Named

@Module(includes = [ResourceProviderModule::class])
class PathProviderModule {
    @Provides
    @Named("skeleton")
    fun provideSkeleton(): SystemPropertyPathProvider {
        return PathProvider("st.dll")
    }

    @Provides
    @Named("fileChooser")
    fun provideSource(): SystemPropertyPathProvider {
        return PathProvider("user.dir")
    }


    @Provides
    @Named("root")
    fun provideRoot(): SystemPropertyPathProvider {
        return PathProvider("compose.application.resources.dir")
    }
}