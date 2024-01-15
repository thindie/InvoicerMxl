package ui.feature_share

import data.util.SystemPropertyPathProvider
import navigation.NavGraph
import ui.feature_share.screen.ShareScreen
import ui.feature_share.screen.state.rememberShareScreenState
import javax.swing.JFileChooser

private const val SHARE = "share"

fun NavGraph.Companion.share(
    pathProvider: SystemPropertyPathProvider,
    fileChooser: JFileChooser,
) {
    this.route(route = SHARE) {
        val screenState =
            rememberShareScreenState(jFileChooser = fileChooser, systemPropertyPathProvider = pathProvider)
        ShareScreen(shareScreenState = screenState)
    }
}