package ui.feature_share

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import navigation.NavGraph

private const val SHARE = "share"

fun NavGraph.Companion.share(
    onClickInvoice: () -> Unit,
) {
    this.route(route = SHARE) {
        Column {
            Text("SHARE")
            Button(onClick = onClickInvoice) { Text("Invoice") }
        }
    }
}