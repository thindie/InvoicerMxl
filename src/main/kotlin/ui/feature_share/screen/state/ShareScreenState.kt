package ui.feature_share.screen.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.util.SystemPropertyPathProvider
import java.io.File
import javax.swing.JFileChooser

@Stable
class ShareScreenState(
    val fileChooser: JFileChooser,
    val pathProvider: SystemPropertyPathProvider,
) {

    var fileChooserType by mutableStateOf(JFileChooser.OPEN_DIALOG)
        private set

    private var lastInvokedFilePicker by mutableStateOf(Picker.NONE)

    val fileChooserTitle by derivedStateOf { title(fileChooser.dialogType == JFileChooser.SAVE_DIALOG) }

    var shouldShowFilePicker by mutableStateOf(false)
        private set

    fun onClickOpenStocking() {
        fileChooserType = JFileChooser.OPEN_DIALOG
        lastInvokedFilePicker = Picker.LOCAL
        shouldShowFilePicker = true
    }


    fun onResult(
        file: File?,
        funcLocal: (String) -> Unit,
        funcResult: (String) -> Unit
    ) {
        if (file != null) {
            when (lastInvokedFilePicker) {
                Picker.LOCAL -> funcLocal(file.absolutePath)
                Picker.NONE -> {}
                Picker.SAVE -> funcResult(file.absolutePath)
            }
        }
        shouldShowFilePicker = false
    }

    private fun title(isSaveFile: Boolean): String {
        return if (isSaveFile) return fileChooserTitleSave else fileChooserTitleOpen
    }

    fun onClickSaveParsedStocking() {
        fileChooserType = JFileChooser.SAVE_DIALOG
        lastInvokedFilePicker = Picker.SAVE
        shouldShowFilePicker = true
    }


    companion object {
        private const val fileChooserTitleOpen = "Open file"
        private const val fileChooserTitleSave = "Save file"
    }

    private enum class Picker {
        LOCAL, NONE, SAVE
    }
}