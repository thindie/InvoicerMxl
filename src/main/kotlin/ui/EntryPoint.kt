package ui

import TITLE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.AppComponent
import ui.main.MainScreen
import javax.inject.Inject
import javax.swing.JFileChooser

class EntryPoint {
    @Inject
    lateinit var viewModel: AppViewModel

    @Inject
    lateinit var fileChooser: JFileChooser

    private var daggerAppComponent: AppComponent? = null


    private fun start() = application {
        initDaggerComponent()
        daggerAppComponent?.inject(this@EntryPoint)

        Window(
            icon = rememberVectorPainter(Icons.Default.List),
            state = rememberWindowState(width = 700.dp, height = 300.dp),
            resizable = true,
            title = TITLE,
            onCloseRequest = ::exitApplication
        ) {
            MainScreen(viewModel, fileChooser)
        }
    }

    companion object {
        fun launchApplication() = EntryPoint().start()
    }

    private fun initDaggerComponent(): AppComponent {
        return if (this.daggerAppComponent == null) {
            AppComponent.init()
        } else checkNotNull(daggerAppComponent)
    }


}
