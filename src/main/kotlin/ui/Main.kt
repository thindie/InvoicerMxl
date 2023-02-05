import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import domain.entities.CentralBaseGoodsVolume
import domain.entities.LocalBaseGoodsVolume
import domain.entities.abstractions.GoodsVolume
import kotlinx.coroutines.delay
import ui.AppModel
import ui.AppModel.ModelState.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

private const val OPEN_LOCAL_RATING = "Open Local Rating File"
private const val OPEN_CENTRAl_RATING = "Open Central Rating File"
const val OPEN_ANOTHER_RATING = "Open Another Rating"
private const val MOCK_CENTRAL_RATING = "Mock Local Rating File"
private const val PARSE = "Parsed"
private const val SAVE_FILE = "Save file"
private const val MOCK = "Mocked"
const val TITLE = "INVOICER MXL for 1c v.7.7"
const val CUT = ""
const val AS = " as "
const val BASE = "Base"

@Composable
fun App() {
    val model: AppModel = AppModel.buildModel()

    MaterialTheme {
        when (model.viewState.collectAsState().value) {
            is SuccessCentral -> {

                model.checkInvoiceStatus()
                SucceedScreen<LocalBaseGoodsVolume>(model)

            }

            is SuccessBothRatings -> {

                FileChooserDialog(SAVE_FILE) { model.onWriteInvoice(it.path) }
                App()
            }

            is SuccessLocal -> {

                model.checkInvoiceStatus()
                SucceedScreen<CentralBaseGoodsVolume>(model)

            }

            is Loading -> {
                StartScreen(model)
            }

            is Error -> {
                ErrorScreen((model.viewState.collectAsState().value as Error).errorCase);
            }
        }
    }
}


fun main() = application {
    Window(
        icon = rememberVectorPainter(Icons.Default.List),
        state = rememberWindowState(width = 700.dp, height = 300.dp),
        resizable = true,
        title = TITLE,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}

@Composable
fun FileChooserDialog(
    title: String, onResult: (result: File) -> Unit
) {
    val fileChooser = JFileChooser(FileSystemView.getFileSystemView())
    fileChooser.currentDirectory = File(System.getProperty("user.dir"))
    fileChooser.dialogTitle = title
    fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
    fileChooser.isAcceptAllFileFilterUsed = true
    fileChooser.selectedFile = null
    fileChooser.currentDirectory = null
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        val file = fileChooser.selectedFile
        println("choose file or folder is: $file")
        onResult(file)
    } else {
//        onResult(java.io.File(""))
        println("No Selection ")
    }
}


@Composable
fun LaunchedEffectLinearProgress() {
    var isLaunchedEffect by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            LaunchedEffect(true) {
                isLaunchedEffect = true
                delay(1500)
                isLaunchedEffect = false
            }

            if (isLaunchedEffect) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(0.4f))
                        LinearProgressIndicator()
                        Spacer(modifier = Modifier.weight(0.4f))
                    }

                }
            }
        }
    }
}

@Composable
fun StartScreen(model: AppModel) {
    var centralButtonDescription by remember { mutableStateOf(OPEN_CENTRAl_RATING) }
    var localButtonDescription by remember { mutableStateOf(OPEN_LOCAL_RATING) }
    var isCentralClicked by remember { mutableStateOf(false) }
    var isLocalClicked by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.weight(1f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    localButtonDescription = PARSE; isLocalClicked = !isLocalClicked
                }) {
                    Text(localButtonDescription)
                    if (isLocalClicked) {
                        FileChooserDialog(TITLE) { model.onGetAllGoods<LocalBaseGoodsVolume>(it.path) };
                        isLocalClicked = !isLocalClicked
                    }

                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = { isCentralClicked = !isCentralClicked;centralButtonDescription = PARSE }
                ) {
                    Text(centralButtonDescription)
                    if (isCentralClicked) {
                        FileChooserDialog(OPEN_CENTRAl_RATING) {
                            model.onGetAllGoods<CentralBaseGoodsVolume>(
                                it.path
                            )
                        }
                        isCentralClicked = !isCentralClicked
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    model.onFillListDirectly(listOf(CentralBaseGoodsVolume(emptyList())));
                    centralButtonDescription = MOCK
                }) {
                    Text(MOCK_CENTRAL_RATING)
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(errorCase: String) {
    var showCard by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.padding(20.dp), elevation = 40.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(errorCase, modifier = Modifier.padding(10.dp))
                Button(onClick = { showCard = true }, Modifier.padding(20.dp)) {
                    Text("OK")

                }
            }
        }
    }

    if (showCard) {
        App()
    }
}

@Composable
inline fun <reified T : GoodsVolume> SucceedScreen(model: AppModel) {
    var isClicked by remember { mutableStateOf(false) }


    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(onClick = { isClicked = !isClicked }) {
                            Text(OPEN_ANOTHER_RATING.plus(AS).plus(T::class.simpleName?.replaceAfter(BASE, CUT)))
                            if (isClicked) {
                                FileChooserDialog(TITLE) { model.onGetAllGoods<T>(it.path) }
                                isClicked = !isClicked
                            }
                        }
                    }
                }

            }
        }
    }
}

