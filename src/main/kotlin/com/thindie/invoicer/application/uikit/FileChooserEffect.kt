package com.thindie.invoicer.application.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import java.io.File
import javax.swing.JFileChooser

@Composable
fun FileChooserEffect(
  fileChooserType: Int,
  path: String,
  title: String,
  onResult: (File?) -> Unit
) {
  val fileChooser = LocalFileChooser.current
  LaunchedEffect(fileChooser) {
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
  }
}

val LocalFileChooser = staticCompositionLocalOf<JFileChooser> { error("No JFileChooser provided") }