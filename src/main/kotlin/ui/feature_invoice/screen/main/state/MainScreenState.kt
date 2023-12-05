package ui.feature_invoice.screen.main.state

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
) {

    var fileChooserType by mutableStateOf(JFileChooser.OPEN_DIALOG)
        private set

    private var lastInvokedFilePicker by mutableStateOf(Picker.NONE)

    val fileChooserTitle by derivedStateOf { title(fileChooser.dialogType == JFileChooser.SAVE_DIALOG) }.apply {
        println(
            this.toString()
        )
    }

    var shouldShowFilePicker by mutableStateOf(false)
        private set

    fun onClickOpenLocalRating() {
        fileChooserType = JFileChooser.OPEN_DIALOG
        lastInvokedFilePicker = Picker.LOCAL
        shouldShowFilePicker = !shouldShowFilePicker
    }

    fun onClickCentralBaseRating() {
        fileChooserType = JFileChooser.OPEN_DIALOG
        lastInvokedFilePicker = Picker.CENTRAL
        shouldShowFilePicker = !shouldShowFilePicker
    }

    fun onResult(
        file: File?,
        funcLocal: (String) -> Unit,
        funcCentral: (String) -> Unit,
        funcResult: (String) -> Unit
    ) {
        if (file != null) {
            when (lastInvokedFilePicker) {
                Picker.CENTRAL -> funcCentral(file.absolutePath)
                Picker.LOCAL -> funcLocal(file.absolutePath)
                Picker.NONE -> { /*ignore*/
                }

                Picker.SAVE -> funcResult(file.absolutePath)
            }
        }
        shouldShowFilePicker = !shouldShowFilePicker
    }

    private fun title(isSaveFile: Boolean): String {
        return if (isSaveFile) return fileChooserTitleSave else fileChooserTitleOpen
    }

    fun onClickSaveSimplyRating() {
        fileChooserType = JFileChooser.SAVE_DIALOG
        lastInvokedFilePicker = Picker.SAVE
        shouldShowFilePicker = !shouldShowFilePicker
    }

    fun onClickSaveMergedRating() {
        fileChooserType = JFileChooser.SAVE_DIALOG
        lastInvokedFilePicker = Picker.SAVE
        shouldShowFilePicker = !shouldShowFilePicker
    }

    companion object {
        private const val fileChooserTitleOpen = "Open file"
        private const val fileChooserTitleSave = "Save file"
    }

    private enum class Picker {
        LOCAL, CENTRAL, NONE, SAVE
    }
}