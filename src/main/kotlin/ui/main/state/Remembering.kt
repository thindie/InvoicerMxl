package ui.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.util.SystemPropertyPathProvider
import javax.swing.JFileChooser

@Composable
fun rememberMainScreenState(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
): MainScreenState {
    return remember() {
        MainScreenState(
            fileChooser = fileChooser,
            pathProvider = systemPropertyPathProvider,
        )
    }
}