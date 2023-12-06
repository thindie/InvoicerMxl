package ui.feature_share.screen.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.util.SystemPropertyPathProvider
import javax.swing.JFileChooser

@Composable
fun rememberShareScreenState(
    jFileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider
): ShareScreenState {
    return remember {
        ShareScreenState(jFileChooser, systemPropertyPathProvider)
    }
}