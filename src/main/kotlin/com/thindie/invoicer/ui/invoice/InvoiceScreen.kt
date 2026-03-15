package com.thindie.invoicer.ui.invoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*
import com.thindie.invoicer.ui.main.Option

@Composable
fun ScreenScope<InvoiceState, InvoiceCommand>.InvoiceScreen(
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
		onBack = { send(InvoiceCommand.Back) }
	  )
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
			onClick = { send(InvoiceCommand.Select(option.id)) }
		  )
		  VSpacer(12.dp)
		}
	  }
	}
  }
}
