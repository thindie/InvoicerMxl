package com.thindie.invoicer

import TITLE
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.thindie.invoicer.application.*
import kotlinx.coroutines.flow.MutableSharedFlow
import ui.theme.InvoicerAppTheme
import javax.swing.UIManager

fun main() = application {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    val router = remember { Router() }
    LaunchedEffect(Unit) {
        router.poppedOut.collect { this@application.exitApplication() }
    }
    InvoicerAppTheme {
        Window(
            icon = rememberVectorPainter(Icons.Default.List),
            state = rememberWindowState(width = 700.dp, height = 400.dp),
            resizable = true,
            title = TITLE,
            onCloseRequest = ::exitApplication
        ) {
            val content = router.currentFlow.collectAsState(initial = null)
            val model = remember { Model(router) }

            LaunchedEffect(Unit) {
                router.push(model.main)
            }
            content.value?.content?.invoke()
        }
    }
}

private val Model.main
    get() = RouteFactory.create(
        { MainState },
        wire = this::mainWire,
        content = { s, c -> MainScreen(s, c) }
    )

private val Model.invoice
    get() = RouteFactory.create(
        { InvoiceState },
        wire = this::invoiceWire,
        content = { s, c -> InvoiceScreen(s, c) }
    )

data object MainState : State
data object InvoiceState : State

sealed interface MainCommand : Command {
    object Invoice : MainCommand
    object Main : MainCommand
    data object Back : MainCommand
}

private

class Model(private val router: Router) {
    fun mainWire(mainCommand: MainCommand): MainState {
        when (mainCommand) {
            MainCommand.Invoice -> {
                router.push(this.invoice)
            }

            MainCommand.Main -> {
                router.push(this.main)
            }

            MainCommand.Back -> router.pop()
        }
        return MainState
    }

    fun invoiceWire(mainCommand: MainCommand): InvoiceState {
        return when (mainCommand) {
            MainCommand.Invoice -> {
                router.push(this.invoice)
                InvoiceState
            }

            MainCommand.Main -> {
                router.push(this.main)
                InvoiceState
            }

            MainCommand.Back -> {
                router.pop()
                InvoiceState
            }
        }
    }
}

@Composable
fun MainScreen(
    state: MainState,
    commands: MutableSharedFlow<MainCommand>
) {
    Column {
        Button(onClick = { commands.send(MainCommand.Invoice) }) {
            Text("Invoice")
        }
        Button(onClick = { commands.send(MainCommand.Back) }) {
            Text("Finish")
        }
    }

}

@Composable
fun InvoiceScreen(
    state: InvoiceState,
    commands: MutableSharedFlow<MainCommand>
) {
    Button(onClick = { commands.send(MainCommand.Main) }) {
        Text("Main")
    }
}