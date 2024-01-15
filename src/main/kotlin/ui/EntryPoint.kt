package ui

import TITLE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.util.SystemPropertyPathProvider
import navigation.NavHost
import navigation.rememberNavController
import root.InvoicerApplication
import ui.feature_from_from_central_invoice.invoiceCentral
import ui.feature_invoice.invoice
import ui.feature_invoice.screen.composables.ControlPanelMainButton
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
                val central = { navController.navigate(Destinations.central)}

                Column(modifier = Modifier.fillMaxSize()) {
                    ControlRow(
                        onClickShare = share,
                        onClickShopRenewInvoice = invoice,
                        onClickAddFromCentral = central,
                    )
                    NavHost(navController = navController) {
                        invoice(
                            fileChooser = fileChooser,
                            systemPropertyPathProvider = pathProvider,
                        )
                        share(
                            pathProvider = pathProvider,
                            fileChooser = fileChooser,
                        )
                        invoiceCentral(fileChooser = fileChooser, systemPropertyPathProvider = pathProvider)
                    }
                }
            }
        }
    }


    @Composable
    private fun ControlRow(modifier: Modifier = Modifier,onClickShare: () -> Unit,
                           onClickShopRenewInvoice: () -> Unit,
                           onClickAddFromCentral: () -> Unit){
        Row(
            modifier = modifier
                .padding(all =20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlPanelMainButton(
                onClickOpenButton = onClickShare,
                title = "Share Supply",
                painter = rememberVectorPainter(Icons.Outlined.Share),
                shouldShowAdditionalSection = false
            )
            ControlPanelMainButton(
                onClickOpenButton = onClickAddFromCentral,
                title = "From Root Supplies",
                painter = rememberVectorPainter(Icons.Outlined.Home),
                shouldShowAdditionalSection = false
            )
            ControlPanelMainButton(
                onClickOpenButton = onClickShopRenewInvoice,
                title = "Update Supply",
                painter = rememberVectorPainter(Icons.Outlined.Create),
                shouldShowAdditionalSection = false
            )
        }
    }

    companion object {
        fun launchApplication() = EntryPoint().onCreate()
    }


}
