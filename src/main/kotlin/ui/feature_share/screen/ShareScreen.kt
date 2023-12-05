package ui.feature_share.screen

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import root.Application.Companion.viewModel
import ui.feature_share.viewmodel.ShareScreenViewModel

@Composable
fun ShareScreen(shareScreenViewModel: ShareScreenViewModel = viewModel(), onClickBack:() -> Unit) {
    Text(text = "hello from ${shareScreenViewModel.viewModelText}")
    Button(onClick = onClickBack){
        Text("BACK")
    }
}