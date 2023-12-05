package ui.feature_invoice

import data.util.SystemPropertyPathProvider
import navigation.NavGraph
import ui.feature_invoice.viewmodel.InvoiceScreenViewModel
import ui.feature_invoice.screen.main.state.InvoiceScreen
import ui.feature_invoice.screen.main.state.rememberMainScreenState
import javax.swing.JFileChooser

private const val INVOICE = "invoice"

fun NavGraph.Companion.invoice(
    viewModel: InvoiceScreenViewModel,
    fileChooser: JFileChooser,
    systemPropertyPathProvider: SystemPropertyPathProvider,
    onNavigateShareStocks: () -> Unit
) {
    this.route(route = INVOICE) {
        val screenState = rememberMainScreenState(fileChooser, systemPropertyPathProvider)
        InvoiceScreen(viewModel = viewModel, state = screenState, onClickShareStocks = onNavigateShareStocks)
    }
}