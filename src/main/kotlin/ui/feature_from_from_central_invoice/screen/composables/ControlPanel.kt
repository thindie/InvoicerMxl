package ui.feature_from_from_central_invoice.screen.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import ui.feature_from_from_central_invoice.viewmodel.InvoiceScreenCentralBaseViewModel
import ui.feature_invoice.screen.composables.ControlPanelMainButton


@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    operationState: InvoiceScreenCentralBaseViewModel.ModelState,
    onClickBaseFilePick: () -> Unit,
    onClickExtraFilePick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlPanelMainButton(
            modifier,
            painter = rememberVectorPainter(Icons.Outlined.ShoppingCart),
            title = "поступление склада",
            shouldShowAdditionalSection = operationState.localFilePath != null,
            onClickOpenButton = onClickBaseFilePick
        )
        Spacer(modifier = modifier.width(12.dp))
        AnimatedVisibility(operationState.localFilePath != null) {
            ControlPanelMainButton(
                modifier,
                painter = rememberVectorPainter(Icons.Outlined.Home),
                title = "инвентаризация",
                onClickOpenButton = onClickExtraFilePick,
                shouldShowAdditionalSection = operationState.mergingFilePath != null
            )
        }

    }
}

