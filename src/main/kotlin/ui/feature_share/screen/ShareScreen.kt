package ui.feature_share.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import root.Application.Companion.viewModel
import ui.FileChooserDialog
import ui.feature_invoice.screen.composables.ControlPanelMainButton
import ui.feature_invoice.screen.composables.InvoiceElement
import ui.feature_share.screen.state.ShareScreenState
import ui.feature_share.viewmodel.ShareScreenViewModel

@Composable
fun ShareScreen(
    modifier: Modifier = Modifier,
    shareScreenViewModel: ShareScreenViewModel = viewModel(),
    shareScreenState: ShareScreenState,
    onClickBack: () -> Unit
) {

    val state by shareScreenViewModel.operationsState.collectAsState()
    Column(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.onSecondary
                    )
                )
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(12.dp))
        Row(modifier = modifier.padding(horizontal = 20.dp)) {
            ControlPanelMainButton(
                modifier,
                title = "Back",
                painter = rememberVectorPainter(Icons.Outlined.ArrowBack),
                onClickBack, shouldShowAdditionalSection = false
            )
        }
        Spacer(modifier = modifier.weight(1f, true))

        Column(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            InvoiceElement(
                modifier = modifier,
                pathTitle = "All stocks:",
                currentPath = state.localFilePath,
                onClickDismiss = shareScreenViewModel::onDismissBasicPath,
                onClickConfirm = { shareScreenViewModel.onClickStartOperation(state.localFilePath ?: "") }
            )


        }

        Row(modifier = modifier.padding(horizontal = 20.dp)) {
            ControlPanelMainButton(
                modifier = modifier,
                title = "Open",
                onClickOpenButton = shareScreenState::onClickOpenStocking,
                shouldShowAdditionalSection = false,
                painter = rememberVectorPainter(Icons.Default.Add)
            )
        }

        AnimatedVisibility(shareScreenState.shouldShowFilePicker) {
            FileChooserDialog(fileChooser = shareScreenState.fileChooser,
                title = shareScreenState.fileChooserTitle,
                fileChooserType = shareScreenState.fileChooserType,
                systemPropertyPathProvider = shareScreenState.pathProvider,
                onResult = {
                    shareScreenState.onResult(
                        it,
                        funcLocal = shareScreenViewModel::onClickOpenLocalStocks,
                        funcResult = shareScreenViewModel::onClickStartOperation
                    )
                })
        }
    }

}