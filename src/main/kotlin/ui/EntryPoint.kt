package ui

import TITLE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.util.SystemPropertyPathProvider
import navigation.NavHost
import navigation.rememberNavController
import root.InvoicerApplication
import ui.feature_invoice.invoice
import ui.feature_share.share
import ui.theme.InvoicerAppTheme
import javax.inject.Inject
import javax.inject.Named
import javax.swing.JFileChooser

class EntryPoint private constructor() : InvoicerApplication() {


    @Inject
    lateinit var fileChooser: JFileChooser

    @Inject
    @Named("fileChooser")
    lateinit var pathProvider: SystemPropertyPathProvider


    init {
        daggerAppComponent?.inject(this@EntryPoint)
    }


    private fun onCreate() = application {
        InvoicerAppTheme {
            Window(
                icon = rememberVectorPainter(Icons.Default.List),
                state = rememberWindowState(width = 700.dp, height = 400.dp),
                resizable = true,
                title = TITLE,
                onCloseRequest = ::exitApplication
            ) {
                val navController = rememberNavController(Destinations.invoice)
                val invoice = { navController.navigate(Destinations.invoice) }
                val share = { navController.navigate(Destinations.share) }

                NavHost(navController = navController) {
                    invoice(
                        fileChooser = fileChooser,
                        systemPropertyPathProvider = pathProvider,
                        onNavigateShareStocks = share
                    )
                    share(
                        pathProvider = pathProvider,
                        fileChooser = fileChooser,
                        onClickInvoice = invoice
                    )
                }
            }
        }
    }

    companion object {
        fun launchApplication() = EntryPoint().onCreate()
    }


}
