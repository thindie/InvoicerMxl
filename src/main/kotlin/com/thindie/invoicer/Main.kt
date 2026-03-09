package com.thindie.invoicer

import TITLE
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.thindie.invoicer.application.Router
import com.thindie.invoicer.application.uikit.LocalFileChooser
import ui.theme.InvoicerAppTheme
import javax.swing.JFileChooser
import javax.swing.UIManager

fun main() = application {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  val router = remember { Router { this@application.exitApplication() } }
  val fileChooser = remember { JFileChooser() }
  InvoicerAppTheme {
	CompositionLocalProvider(
	  LocalFileChooser provides fileChooser,
	) {
	  Window(
		icon = rememberVectorPainter(Icons.Default.List),
		state = rememberWindowState(width = 700.dp, height = 400.dp),
		resizable = true,
		title = TITLE,
		onCloseRequest = ::exitApplication
	  ) {
		remember { ApplicationFlow(router).start() }
		val routes by router.route.collectAsState(null)
		if (routes != null) {
		  AnimatedContent(
			targetState = routes!!.first,
		  ) { route -> route.content.invoke() }
		}
	  }
	}
  }
}
