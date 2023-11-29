package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import data.util.SystemPropertyPathProvider
import java.io.File
import javax.swing.JFileChooser

@Composable
fun FileChooserDialog(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
    title: String,
    onResult: (File?) -> Unit
) {
    with(fileChooser) {

        currentDirectory = File(System.getProperty(systemPropertyPathProvider.getProperty()))
        dialogTitle = title
        fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        isAcceptAllFileFilterUsed = true
        selectedFile = null
        currentDirectory = null

    }

    val chooserOpenDialogReturns by remember { mutableStateOf(fileChooser.showOpenDialog(null)) }


    when (chooserOpenDialogReturns) {
        JFileChooser.APPROVE_OPTION -> {
            onResult.invoke(fileChooser.selectedFile)
            return
        }

        JFileChooser.CANCEL_OPTION -> {
            return
        }

        JFileChooser.ERROR_OPTION -> {
            return
        }

    }

}


