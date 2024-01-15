package ui.feature_invoice.screen.main

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import domain.entities.Engine
import root.Application.Companion.viewModel
import ui.FileChooserDialog
import ui.feature_invoice.screen.composables.ControlPanel
import ui.feature_invoice.screen.composables.InvoiceElement
import ui.feature_invoice.screen.composables.ScreenHint
import ui.feature_invoice.screen.composables.invoiceScreenHint
import ui.feature_invoice.screen.main.state.InvoiceScreenState
import ui.feature_invoice.viewmodel.InvoiceScreenViewModel

@Composable
fun InvoiceScreen(
    modifier: Modifier = Modifier,
    viewModel: InvoiceScreenViewModel = viewModel(),
    state: InvoiceScreenState,
 ) {
    val viewModelState by viewModel.operationsState.collectAsState()

    Column(
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary
                )
            )
        ).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Заказываем!",
                style = MaterialTheme.typography.headlineMedium.copy(MaterialTheme.colorScheme.onSurface)
            )
        }
        ScreenHint(hint = invoiceScreenHint)
        Spacer(modifier = modifier.height(12.dp))
        AnimatedVisibility(viewModelState.state == Engine.STANDBY) {
            Column(
                modifier = modifier.fillMaxWidth().animateContentSize()
            ) {
                InvoiceElement(
                    modifier = modifier,
                    pathTitle = "My rating:",
                    currentPath = viewModelState.localFilePath,
                    onClickDismiss = {
                        viewModel.onDismissExtraPath()
                        viewModel.onDismissBasicPath()
                    },
                    onClickConfirm = state::onClickSaveSimplyRating
                )
                Spacer(modifier = modifier.height(12.dp))
                InvoiceElement(
                    modifier = modifier,
                    pathTitle = "Merging:",
                    currentPath = viewModelState.mergingFilePath,
                    onClickDismiss = viewModel::onDismissExtraPath,
                    onClickConfirm = state::onClickSaveMergedRating
                )
            }
        }
        Spacer(modifier = modifier.height(12.dp))
        ControlPanel(
            onClickBaseFilePick = state::onClickOpenLocalRating,
            onClickExtraFilePick = state::onClickCentralBaseRating,
            operationState = viewModelState
        )
        Spacer(modifier = modifier.height(12.dp))
        AnimatedVisibility(viewModelState.state == Engine.LOAD) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
        }
    }
    if (state.shouldShowFilePicker) {
        FileChooserDialog(fileChooser = state.fileChooser,
            title = state.fileChooserTitle,
            systemPropertyPathProvider = state.pathProvider,
            fileChooserType = state.fileChooserType,
            onResult = { file ->
                state.onResult(
                    file,
                    viewModel::onClickOpenLocalRating,
                    viewModel::onClickOpenMergingRating,
                    viewModel::onClickStartOperation
                )
            })
    }

}

