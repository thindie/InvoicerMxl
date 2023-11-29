package ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui.AppViewModel
import ui.FileChooserDialog
import ui.main.state.MainScreenState

@Composable
fun MainScreen(
    viewModel: AppViewModel,
    state: MainScreenState
) {


    MaterialTheme {

        val viewModelState by viewModel.operationsState.collectAsState()
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(viewModelState.localFilePath.orEmpty())
            Button(onClick = state::onClickOpenLocalRating) {
                Text(
                    text = MainScreenState.localRatingButtonTitle,
                    style = LocalTextStyle.current.copy(color = if (viewModelState.isError) Color.Red else Color.Black)
                )
            }
        }
        AnimatedVisibility(viewModelState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.shouldShowFilePicker) {
            FileChooserDialog(
                fileChooser = state.fileChooser,
                title = "FileChooser",
                systemPropertyPathProvider = state.pathProvider,
                onResult = {
                    state.onResult(it, viewModel::onClickOpenLocalRating)
                })
        }

    }
}
