package ui.feature_invoice

import data.util.SystemPropertyPathProvider
import navigation.NavGraph
import ui.AppViewModel
import ui.feature_invoice.main.state.InvoiceScreen
import ui.feature_invoice.main.state.rememberMainScreenState
import javax.swing.JFileChooser

private const val INVOICE = "invoice"

fun NavGraph.Companion.invoice(
    viewModel: AppViewModel,
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
    onNavigateShareStocks: () -> Unit
) {
    this.route(route = INVOICE) {
        val screenState = rememberMainScreenState(fileChooser, systemPropertyPathProvider)
        InvoiceScreen(viewModel = viewModel, state = screenState, onClickShareStocks = onNavigateShareStocks)
    }
}