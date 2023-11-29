package ui.main.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.util.SystemPropertyPathProvider
import java.io.File
import javax.swing.JFileChooser

@Stable
class MainScreenState(
    val fileChooser: JFileChooser,
    val pathProvider: SystemPropertyPathProvider,
    val isSaveJChooser: Boolean,

    ) {

    val fileChooserTitle by derivedStateOf { title(isSaveJChooser) }

    var shouldShowFilePicker by mutableStateOf(false)
        private set

    fun onClickOpenLocalRating() {
        shouldShowFilePicker = !shouldShowFilePicker
    }

    fun onResult(file: File?, func: (String) -> Unit) {
        if (file != null) {
            func.invoke(file.absolutePath)
        }
        onClickOpenLocalRating()
    }

    private fun title(isSaveFile: Boolean): String {
        return if (isSaveFile) return fileChooserTitleSave else fileChooserTitleOpen
    }

    companion object {
        const val localRatingButtonTitle = "Pick local"
        private const val fileChooserTitleOpen = "Open file"
        private const val fileChooserTitleSave = "Save file"
    }
}