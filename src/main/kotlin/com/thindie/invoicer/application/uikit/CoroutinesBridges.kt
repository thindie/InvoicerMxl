package com.thindie.invoicer.application.uikit

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.thindie.invoicer.application.error.AppError
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.Image
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.swing.JFileChooser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun openFileChooser(
  fileChooserType: Int,
  path: String = "user.dir",
  title: String,
  fileChooser: JFileChooser,
): File? {
  return suspendCancellableCoroutine { continuation ->
	with(fileChooser) {
	  dialogType = fileChooserType
	  currentDirectory = File(System.getProperty(path))
	  dialogTitle = title
	  fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
	  isAcceptAllFileFilterUsed = true
	  selectedFile = null
	  currentDirectory = null
	}

	when (fileChooser.showOpenDialog(null)) {
	  JFileChooser.APPROVE_OPTION -> {
		val file = fileChooser.selectedFile

		continuation.resume(file)
	  }

	  JFileChooser.ERROR_OPTION -> {
		continuation.resumeWithException(IllegalStateException("File is not selected"))
	  }

	  JFileChooser.CANCEL_OPTION -> {
		continuation.resume(null)
	  }
	}
  }
}

suspend fun openSaveFileChooser(
  fileChooser: JFileChooser,
  suggestedFileName: String,
  title: String,
): File? {
  return suspendCancellableCoroutine { continuation ->
	with(fileChooser) {
	  dialogType = JFileChooser.SAVE_DIALOG
	  dialogTitle = title
	  fileSelectionMode = JFileChooser.FILES_ONLY
	  isAcceptAllFileFilterUsed = true
	  selectedFile = File(suggestedFileName)
	}

	when (fileChooser.showSaveDialog(null)) {
	  JFileChooser.APPROVE_OPTION -> {
		continuation.resume(fileChooser.selectedFile)
	  }

	  JFileChooser.ERROR_OPTION -> {
		continuation.resumeWithException(IllegalStateException("Save dialog error"))
	  }

	  JFileChooser.CANCEL_OPTION -> {
		continuation.resume(null)
	  }
	}
  }
}

suspend fun getImageBitmap(resourcePath: String, resdir: File): ImageBitmap? {
  return suspendCancellableCoroutine { continuation ->
	val file = resdir.resolve(resourcePath)
	if (!file.exists()) {
	  continuation.resumeWithException(IllegalStateException("Directory $file does not exist."))
	}
	val image = try {
	  val bytes = file.readBytes()
	  Image.makeFromEncoded(bytes).toComposeImageBitmap()
	} catch (e: Exception) {
	  continuation.resumeWithException(AppError.FileReadError(e.cause, e.message))
	  null
	}
	if (image != null) {
	  continuation.resume(image)
	} else {
	  continuation.resumeWithException(IllegalStateException("Image could not be read"))
	}
  }
}

suspend fun extractRatingHelper(resdir: File, resourcePath: String, destination: File) {
  return suspendCancellableCoroutine { continuation ->
	val source = resdir.resolve(resourcePath)
	if (!source.exists()) {
	  continuation.resumeWithException(IllegalStateException("Directory $source does not exist."))
	}
	try {
	  Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
	} catch (e: Exception) {
	  continuation.resumeWithException(AppError.FileWriteError(e.cause, e.message))
	}
	continuation.resume(Unit)
  }
}

val LocalFileChooser = staticCompositionLocalOf<JFileChooser> { error("No JFileChooser provided") }