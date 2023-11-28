package ui

import androidx.compose.runtime.Composable
import java.io.File
import javax.swing.JFileChooser

@Composable
fun FileChooserDialog(
    fileChooser: JFileChooser,
    title: String,
    onResult: (File) -> Unit
) {
    with(fileChooser){
        currentDirectory = File(System.getProperty("user.dir"))
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