package ui.feature_invoice.screen.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.feature_invoice.viewmodel.InvoiceScreenViewModel


@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    operationState: InvoiceScreenViewModel.ModelState,
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
            title = "мои остатки",
            shouldShowAdditionalSection = operationState.localFilePath != null,
            onClickOpenButton = onClickBaseFilePick
        )
        Spacer(modifier = modifier.width(12.dp))
        AnimatedVisibility(operationState.localFilePath != null) {
            ControlPanelMainButton(
                modifier,
                title = "остатки склада",
                onClickOpenButton = onClickExtraFilePick,
                shouldShowAdditionalSection = operationState.mergingFilePath != null
            )
        }

    }
}

@Composable
private fun RowScope.ControlPanelMainButton(
    modifier: Modifier = Modifier,
    title: String,
    onClickOpenButton: () -> Unit,
    shouldShowAdditionalSection: Boolean
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(all = 4.dp),
        enabled = shouldShowAdditionalSection.not(),
        onClick = {

            onClickOpenButton()
        }, modifier = modifier
            .align(Alignment.CenterVertically)
    ) {
        Text(title)
    }
    Spacer(modifier = modifier.width(12.dp))
}
