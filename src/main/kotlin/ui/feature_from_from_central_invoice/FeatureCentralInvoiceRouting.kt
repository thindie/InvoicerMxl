package ui.feature_from_from_central_invoice

import data.util.SystemPropertyPathProvider
import navigation.NavGraph
import ui.feature_from_from_central_invoice.screen.main.InvoiceFromCentralScreen
import ui.feature_from_from_central_invoice.screen.main.state.rememberInvoiceFromCentralScreenState
import javax.swing.JFileChooser

private const val INVOICE_CENTRAL = "invoice_central"

fun NavGraph.Companion.invoiceCentral(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
) {
    this.route(route = INVOICE_CENTRAL) {
        val screenState = rememberInvoiceFromCentralScreenState(fileChooser, systemPropertyPathProvider)
        InvoiceFromCentralScreen(state = screenState)
    }
}