package ui.feature_invoice

import data.util.SystemPropertyPathProvider
import navigation.NavGraph
import ui.feature_invoice.screen.main.InvoiceScreen
import ui.feature_invoice.screen.main.state.rememberInvoiceScreenState
import javax.swing.JFileChooser

private const val INVOICE = "invoice"

fun NavGraph.Companion.invoice(
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
    onNavigateShareStocks: () -> Unit
) {
    this.route(route = INVOICE) {
        val screenState = rememberInvoiceScreenState(fileChooser, systemPropertyPathProvider)
        InvoiceScreen(state = screenState, onClickShareStocks = onNavigateShareStocks)
    }
}