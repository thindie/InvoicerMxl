package di

import dagger.Module
import dagger.Provides
import data.util.SystemPropertyPathProvider
import java.io.File
import javax.inject.Named

@Module
class ResourceProviderModule {
    @Provides
    fun provideResourcesFile(@Named("root") pathProvider: SystemPropertyPathProvider): File {
        return File(System.getProperty(pathProvider.getProperty()))
    }
}