package ui

import TITLE
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.util.SystemPropertyPathProvider
import di.AppComponent
import ui.main.MainScreen
import ui.main.state.rememberMainScreenState
import javax.inject.Inject
import javax.inject.Named
import javax.swing.JFileChooser

class EntryPoint private constructor() {
    @Inject
    lateinit var viewModel: AppViewModel

    @Inject
    lateinit var fileChooser: JFileChooser

    @Inject
    @Named("fileChooser")
    lateinit var pathProvider: SystemPropertyPathProvider

    private var daggerAppComponent: AppComponent? = null

    init {
        daggerAppComponent = initDaggerComponent()
        daggerAppComponent?.inject(this@EntryPoint)
    }


    private fun start() = application {
        MaterialTheme {
            Window(
                icon = rememberVectorPainter(Icons.Default.List),
                state = rememberWindowState(width = 700.dp, height = 300.dp),
                resizable = true,
                title = TITLE,
                onCloseRequest = ::exitApplication
            ) {
                MainScreen(
                    viewModel = viewModel,
                    state = rememberMainScreenState(fileChooser, pathProvider, isSaveJChooser = false)
                )
            }
        }
    }

    private fun initDaggerComponent(): AppComponent {
        return if (this.daggerAppComponent == null) {
            AppComponent.init()
        } else checkNotNull(daggerAppComponent)
    }

    companion object {
        fun launchApplication() = EntryPoint().start()
    }


}
