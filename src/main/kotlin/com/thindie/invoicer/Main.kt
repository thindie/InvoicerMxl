package com.thindie.invoicer

import TITLE
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.thindie.invoicer.application.Route
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
		var prev by remember { mutableStateOf<Pair<Route, Route?>?>(null) }
		val isPop = routes != null && prev != null && routes!!.first == prev!!.second
		LaunchedEffect(routes) { prev = routes }
		if (routes != null) {
		  val tween = tween<IntOffset>(durationMillis = 280)
		  AnimatedContent(
			targetState = routes!!.first,
			transitionSpec = {
			  if (isPop) {
				slideInHorizontally(tween) { -it } + fadeIn(tween()) togetherWith
					slideOutHorizontally(tween) { it } + fadeOut(tween())
			  } else {
				slideInHorizontally(tween) { it } + fadeIn(tween()) togetherWith
					slideOutHorizontally(tween) { -it } + fadeOut(tween())
			  }
			},
			label = "route"
		  ) { route -> route.content.invoke() }
		}
	  }
	}
  }
}
