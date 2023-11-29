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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import data.util.SystemPropertyPathProvider
import ui.AppViewModel
import ui.FileChooserDialog
import javax.swing.JFileChooser

@Composable
fun MainScreen(
    viewModel: AppViewModel, fileChooser: JFileChooser, pathProvider: SystemPropertyPathProvider
) {


    MaterialTheme {
        var shouldShowFilePicker by remember { mutableStateOf(false) }
        val viewModelState by viewModel.operationsState.collectAsState()
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(viewModelState.localFilePath.orEmpty())
            Button(onClick = {
                shouldShowFilePicker = true
            }) {
                Text(
                    "open file picker",
                    style = LocalTextStyle.current.copy(color = if (viewModelState.isError) Color.Red else Color.Black)
                )
            }
        }
        AnimatedVisibility(viewModelState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        AnimatedVisibility(visible = shouldShowFilePicker) {
            FileChooserDialog(
                fileChooser = fileChooser,
                title = "FileChooser",
                systemPropertyPathProvider = pathProvider,
                onResult = {
                    viewModel.onClickOpenLocalRating(it.absolutePath)
                })
        }
    }
}
