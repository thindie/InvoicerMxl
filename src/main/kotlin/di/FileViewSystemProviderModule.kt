package di

import dagger.Module
import dagger.Provides
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

@Module
class FileViewSystemProviderModule {
    @Provides
    fun provideFileViewSystem(): JFileChooser {
        return JFileChooser(FileSystemView.getFileSystemView())
    }
}