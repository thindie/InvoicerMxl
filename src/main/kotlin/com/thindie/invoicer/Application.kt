package com.thindie.invoicer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.thindie.invoicer.application.Route
import com.thindie.invoicer.application.Router
import com.thindie.invoicer.application.uikit.*
import com.thindie.invoicer.application.update.AppVersion
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager

private const val TITLE = "Invoicer MXL 1C 7.7, version ${AppVersion.SEMVER}"

fun main() = application {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  val router = remember { Router { this@application.exitApplication() } }
  val fileChooser = remember { JFileChooser() }
  val themeSwitcher = remember { ThemeSwitcher() }
  val resourcesDir = remember {
	System.getProperty("compose.application.resources.dir")
	  ?.let { File(it) }
	  ?: File("src/appData/common")
  }
  CompositionLocalProvider(
	LocalThemeSwitcher provides themeSwitcher,
	LocalResourceDir provides resourcesDir,
  ) {
	val themeColors = LocalThemeSwitcher.current.themeFlow.collectAsState(null)
	val isDark = when (themeColors.value) {
	  null -> isSystemInDarkTheme()
	  ThemeSwitcher.Choice.Dark -> true
	  ThemeSwitcher.Choice.Light -> false
	  ThemeSwitcher.Choice.Auto -> isSystemInDarkTheme()
	}
	InvoicerTheme(isDark) {
	  CompositionLocalProvider(
		LocalFileChooser provides fileChooser,
	  ) {
		Window(
		  icon = rememberVectorPainter(Icons.Default.List),
		  state = rememberWindowState(width = 700.dp, height = 954.dp),
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
			  modifier = Modifier.background(InvoicerTheme.colors.backgroundPrimary),
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
}

