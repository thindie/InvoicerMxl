package ui.feature_from_from_central_invoice.screen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Composable
fun SharePanel(modifier: Modifier = Modifier, onClickShare: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ControlPanelMainButton(
            onClickOpenButton = onClickShare,
            title = "Share",
            painter = rememberVectorPainter(Icons.Outlined.Share),
            shouldShowAdditionalSection = false
        )
    }
}