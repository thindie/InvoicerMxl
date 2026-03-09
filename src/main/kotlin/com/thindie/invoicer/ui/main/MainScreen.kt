package com.thindie.invoicer.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.AppScreen
import com.thindie.invoicer.application.uikit.Button
import com.thindie.invoicer.application.uikit.SentenceRow
import com.thindie.invoicer.application.uikit.VSpacer


@Composable
fun ScreenScope<MainState, MainCommand>.MainScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier.fillMaxSize()
		.background(color = MaterialTheme.colorScheme.surfaceContainer)
		.padding(16.dp),
	  verticalArrangement = Arrangement.SpaceEvenly,
	  horizontalAlignment = Alignment.CenterHorizontally
	) {
	  IconButton(
		modifier = Modifier.align(Alignment.Start),
		onClick = { send(MainCommand.Exit) }
	  ) {
		Icon(
		  painter = rememberVectorPainter(Icons.Default.Close),
		  contentDescription = null,
		  tint = MaterialTheme.colorScheme.onPrimary
		)
	  }
	  state.value.options.forEach { option ->
		key(option) {
		  SentenceRow(
			modifier = Modifier
			  .let {
				if (option.id == state.value.selected?.id) {
				  it
					.border(
					  BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
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
