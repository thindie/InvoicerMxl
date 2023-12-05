package ui.feature_share

import navigation.NavGraph
import ui.feature_share.screen.ShareScreen

private const val SHARE = "share"

fun NavGraph.Companion.share(
    onClickInvoice: () -> Unit,
) {
    this.route(route = SHARE) {
        ShareScreen(onClickBack = onClickInvoice)
    }
}