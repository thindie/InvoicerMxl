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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
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
            painter = rememberVectorPainter(Icons.Outlined.ShoppingCart),
            title = "мои остатки",
            shouldShowAdditionalSection = operationState.localFilePath != null,
            onClickOpenButton = onClickBaseFilePick
        )
        Spacer(modifier = modifier.width(12.dp))
        AnimatedVisibility(operationState.localFilePath != null) {
            ControlPanelMainButton(
                modifier,
                painter = rememberVectorPainter(Icons.Outlined.Home),
                title = "остатки склада",
                onClickOpenButton = onClickExtraFilePick,
                shouldShowAdditionalSection = operationState.mergingFilePath != null
            )
        }

    }
}

@Composable
fun RowScope.ControlPanelMainButton(
    modifier: Modifier = Modifier,
    title: String,
    painter: VectorPainter? = null,
    onClickOpenButton: () -> Unit,
    shouldShowAdditionalSection: Boolean
) {
    TextButton(
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(all = 4.dp),
        enabled = shouldShowAdditionalSection.not(),
        onClick = onClickOpenButton,
        modifier = modifier
            .align(Alignment.CenterVertically),

        ) {
        if (painter != null) {
            Icon(painter = (painter), contentDescription = null)
        }

        Text(title, style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold))
    }
}
