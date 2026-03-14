package com.thindie.invoicer.application.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import java.io.File
import java.util.UUID
import javax.swing.JFileChooser

@Composable
fun FileChooserEffect(
  fileChooserType: Int,
  path: String = "user.dir",
  title: String,
  onResult: (File?) -> Unit,
  onCancel: () -> Unit = {},
  onError: () -> Unit = {}
) {
  val fileChooser = LocalFileChooser.current
  SideEffect {
	with(fileChooser) {
	  dialogType = fileChooserType
	  currentDirectory = File(System.getProperty(path))
	  dialogTitle = title
	  fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
	  isAcceptAllFileFilterUsed = true
	  selectedFile = null
	  currentDirectory = null
	}
  }
  when (fileChooser.showOpenDialog(null)) {
	JFileChooser.APPROVE_OPTION -> {
	  onResult.invoke(fileChooser.selectedFile)
	}
	JFileChooser.ERROR_OPTION -> {
	  onError.invoke()
	}
	JFileChooser.CANCEL_OPTION -> {
	  onCancel.invoke()
	}
  }
}

val LocalFileChooser = staticCompositionLocalOf<JFileChooser> { error("No JFileChooser provided") }