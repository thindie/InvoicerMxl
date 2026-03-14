package com.thindie.invoicer.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*


@Composable
fun ScreenScope<MainState, MainCommand>.MainScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier.fillMaxSize()
		.background(color = InvoicerTheme.colors.backgroundPrimary)
		.padding(16.dp),
	  verticalArrangement = Arrangement.SpaceEvenly,
	  horizontalAlignment = Alignment.CenterHorizontally
	) {
	  Row {
		IconButton(
		  onClick = { send(MainCommand.Exit) }
		) {
		  Icon(
			painter = rememberVectorPainter(Icons.Default.Close),
			contentDescription = null,
			tint = InvoicerTheme.colors.accentPrimary
		  )
		  WSpacer()
		  val switcher = LocalThemeSwitcher.current
		  val currentTheme by switcher.themeFlow.collectAsState(null)
		  IconButton(
			onClick = {
			  val choice = when (currentTheme) {
				ThemeSwitcher.Choice.Dark -> ThemeSwitcher.Choice.Light
				ThemeSwitcher.Choice.Light -> ThemeSwitcher.Choice.Dark
				ThemeSwitcher.Choice.Auto -> ThemeSwitcher.Choice.Auto
				null -> ThemeSwitcher.Choice.Dark
			  }
			  switcher.set(choice)
			}
		  ) {
			Icon(
			  painter = rememberVectorPainter(Icons.TwoTone.Settings),
			  contentDescription = null,
			  tint = InvoicerTheme.colors.accentPrimary
			)
		  }
		}
	  }

	  state.value.options.forEach { option ->
		key(option) {
		  SentenceRow(
			modifier = Modifier
			  .let {
				if (option.id == state.value.selected?.id) {
				  it
					.border(
					  BorderStroke(width = 1.dp, color = InvoicerTheme.colors.successPrimary),
					  shape = RoundedCornerShape(20.dp)
					)
				} else {
				  it
				}
			  }
			  .fillMaxWidth(),
			title = option.title,
			subtitle = option.subtitle,
			loading = false,
			painter = option.image?.let { rememberVectorPainter(it) },
			onClick = { send(MainCommand.ToggleOption(option.id)) }
		  )
		  VSpacer(12.dp)
		}
	  }
	  Button(
		onClick = { send(MainCommand.Confirm) },
		enabled = state.value.selected != null,
		text = "Confirm"
	  )
	}
  }
}
