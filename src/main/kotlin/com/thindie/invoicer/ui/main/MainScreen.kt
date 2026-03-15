package com.thindie.invoicer.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
	  horizontalAlignment = Alignment.CenterHorizontally
	) {
	  TopAppBar(
		title = "Выбери желаемое",
		description = "пока доступно только простое обновление ассортимента",
		onClose = { send(MainCommand.Exit) }
	  )
	  VSpacer(24.dp)
	  Row {
		val switcher = LocalThemeSwitcher.current
		val currentTheme by switcher.themeFlow.collectAsState(null)

		val dayColor by animateColorAsState(
		  when (currentTheme) {
			ThemeSwitcher.Choice.Dark -> InvoicerTheme.colors.contentSecondary
			else -> InvoicerTheme.colors.accentPrimary
		  }
		)
		val nightColor by animateColorAsState(
		  when (currentTheme) {
			ThemeSwitcher.Choice.Dark -> InvoicerTheme.colors.accentPrimary
			else -> InvoicerTheme.colors.contentSecondary
		  }
		)
		WSpacer()
		Text(
		  modifier = Modifier.clickable(
			indication = null,
			interactionSource = remember { MutableInteractionSource() },
			onClick = { switcher.set(ThemeSwitcher.Choice.Light) }
		  ),
		  text = "День",
		  color = dayColor
		)
		Text(
		  text = " | ",
		  color = InvoicerTheme.colors.backgroundSecondary
		)
		Text(
		  modifier = Modifier.clickable(
			indication = null,
			interactionSource = remember { MutableInteractionSource() },
			onClick = { switcher.set(ThemeSwitcher.Choice.Dark) }
		  ),
		  text = "Hочь",
		  color = nightColor
		)
	  }
	  VSpacer(24.dp)

	  state.value.options.forEach { option ->
		key(option) {
		  SentenceRow(
			modifier = Modifier
			  .border(
				BorderStroke(width = 1.2.dp, color = InvoicerTheme.colors.backgroundSecondary),
				shape = RoundedCornerShape(20.dp)
			  )
			  .fillMaxWidth(),
			title = option.title,
			subtitle = option.subtitle,
			loading = false,
			enabled = option is Option.Invoice,
			painter = option.image?.let { rememberVectorPainter(it) },
			onClick = { send(MainCommand.Select(option.id)) }
		  )
		  VSpacer(12.dp)
		}
	  }
	  WSpacer()
	  Button(
		text = "Выход",
		onClick = { send(MainCommand.Exit) },
	  )
	}
  }
}
