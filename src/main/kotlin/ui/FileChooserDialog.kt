package ui

import androidx.compose.runtime.Composable
import data.util.SystemPropertyPathProvider
import java.io.File
import javax.swing.JFileChooser

@Composable
fun FileChooserDialog(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
    title: String,
    onResult: (File) -> Unit
) {
    with(fileChooser){
        currentDirectory = File(System.getProperty(systemPropertyPathProvider.getProperty()))
        dialogTitle = title
        fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        isAcceptAllFileFilterUsed = true
        selectedFile = null
        currentDirectory = null
    }

    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        onResult(fileChooser.selectedFile)
    }
}