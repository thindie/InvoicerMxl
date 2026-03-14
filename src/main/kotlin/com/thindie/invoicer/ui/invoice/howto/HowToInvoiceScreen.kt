package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*

@Composable
fun ScreenScope<HowToIntroState, HowToInvoiceCommand>.HowToIntroScreen() {
  HowToScreen()
}

@Composable
fun ScreenScope<HowToStep0State, HowToInvoiceCommand>.HowToStep0Screen() {
  HowToScreen()
}

@Composable
fun ScreenScope<HowToStep1State, HowToInvoiceCommand>.HowToStep1Screen() {
  HowToScreen()
}

@Composable
fun ScreenScope<HowToStep2State, HowToInvoiceCommand>.HowToStep2Screen() {
  HowToScreen()
}

@Composable
private fun <S : HowToInvoiceState> ScreenScope<S, HowToInvoiceCommand>.HowToScreen() {
  val state = state.value
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = title,
		onBack = { send(HowToInvoiceCommand.Back) },
	  )

	  WSpacer()

	  Text(
		modifier = Modifier
		  .background(InvoicerTheme.colors.accentPrimary)
		  .padding(8.dp),
		text = state.question,
		style = InvoicerTheme.typography.headlineSmall,
		color = InvoicerTheme.colors.contentPrimary,
		textAlign = TextAlign.Start,
	  )

	  state.answer?.let { answer ->
		VSpacer(8.dp)
		Text(
		  modifier = Modifier
			.fillMaxWidth()
			.background(InvoicerTheme.colors.backgroundSecondary)
			.padding(8.dp),
		  text = answer,
		  style = InvoicerTheme.typography.bodyMedium,
		  color = InvoicerTheme.colors.contentPrimary,
		)
	  }

	  state.extraExplanation?.let { extra ->
		val resDir = LocalResourceDir.current
		val chooser = LocalFileChooser.current
		var showDialog by remember { mutableStateOf(false) }
		if (showDialog) {
		  AlertDialog(
			onDismissRequest = {
			  showDialog = false
			  send(HowToInvoiceCommand.RequestRatingHelper(chooser, resDir))
			},
			shape = RoundedCornerShape(20.dp),
			title = {
			  Text(
				modifier = Modifier
				  .background(InvoicerTheme.colors.backgroundSecondary)
				  .padding(8.dp),
				text = "Внимание",
				style = InvoicerTheme.typography.headlineSmall
			  )
			},
			text = { Text("Сейчас будем выбирать место, куда положить выгрузку-хелпер. \n Не забудь про .ert") },
			buttons = {
			  Button(
				modifier = Modifier.fillMaxWidth().padding(16.dp),
				text = "Готов, сохранять в .ert это пушка!",
				onClick = {
				  showDialog = false
				  send(HowToInvoiceCommand.RequestRatingHelper(chooser, resDir))
				}
			  )
			},
		  )
		}
		ClickableText(
		  modifier = Modifier
			.fillMaxWidth()
			.background(InvoicerTheme.colors.backgroundSecondary)
			.padding(8.dp),
		  text = extra,
		  style = InvoicerTheme.typography.bodyMedium.copy(color = InvoicerTheme.colors.contentPrimary),
		  onClick = { showDialog = true }
		)
	  }

	  state.image?.let { bitmap ->
		VSpacer(16.dp)
		StepImage(
		  imageBitmap = bitmap,
		  modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = 200.dp, max = 900.dp),
		)
	  }

	  WSpacer()
	  VSpacer(16.dp)
	  Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
	  ) {
		val file = LocalResourceDir.current
		Button(
		  text = state.primaryAction,
		  onClick = { send(HowToInvoiceCommand.Next(file)) },
		)
	  }
	}
  }
}


@Composable
private fun StepImage(
  imageBitmap: ImageBitmap,
  modifier: Modifier = Modifier,
) {
  Image(
	modifier = modifier,
	bitmap = imageBitmap,
	contentDescription = null,
	contentScale = ContentScale.Fit,
  )
}

private const val title = "Как получить файл рейтинга"