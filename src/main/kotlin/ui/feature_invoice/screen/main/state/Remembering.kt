package ui.feature_invoice.screen.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.util.SystemPropertyPathProvider
import javax.swing.JFileChooser

@Composable
fun rememberInvoiceScreenState(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
): InvoiceScreenState {
    return remember() {
        InvoiceScreenState(
            fileChooser = fileChooser,
            pathProvider = systemPropertyPathProvider,
        )
    }
}