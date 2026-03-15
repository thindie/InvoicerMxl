package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
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


	  SentenceRow(
		title = state.question,
		painter = rememberVectorPainter(Icons.Default.Info),
		subtitle = null,
		onClick = null,
		enabled = false,
		loading = false,
	  )

	  state.answer?.let { answer ->
		VSpacer(8.dp)
		SentenceRow(
		  title = "Что делаем:",
		  painter = rememberVectorPainter(Icons.Default.Done),
		  subtitle = answer,
		  onClick = null,
		  enabled = false,
		  loading = false,
		)
		VSpacer(8.dp)
	  }

	  state.extraExplanation?.let { extra ->
		val resDir = LocalResourceDir.current
		val chooser = LocalFileChooser.current
		var showDialog by remember { mutableStateOf(false) }
		if (showDialog) {
		  AlertDialog(
			backgroundColor = InvoicerTheme.colors.backgroundSecondary,
			onDismissRequest = {
			  showDialog = false
			  send(HowToInvoiceCommand.RequestRatingHelper(chooser, resDir))
			},
			shape = RoundedCornerShape(20.dp),
			text = {
			  SentenceRow(
				title = "Выбери место!",
				painter = rememberVectorPainter(Icons.Default.Info),
				subtitle = "\n После того, как кликнешь ок - будем выбирать место, куда положить выгрузку-хелпер. \n Не забудь про .ert",
				onClick = null,
				enabled = false,
				loading = false,
			  )
			},
			buttons = {
			  Button(
				modifier = Modifier.fillMaxWidth().padding(16.dp),
				text = "Готов",
				onClick = {
				  showDialog = false
				  send(HowToInvoiceCommand.RequestRatingHelper(chooser, resDir))
				}
			  )
			},
		  )
		}
		SentenceRow(
		  modifier = Modifier.border(
			BorderStroke(1.dp, InvoicerTheme.colors.backgroundSecondary),
			shape = RoundedCornerShape(20.dp),
		  ),
		  title = "Чтобы не искать:",
		  painter = rememberVectorPainter(Icons.Default.Add),
		  subtitle = extra,
		  onClick = { showDialog = true },
		  enabled = true,
		  loading = false,
		)
	  }

	  state.image?.let { bitmap ->
		StepImage(
		  imageBitmap = bitmap,
		  modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = 200.dp, max = 840.dp),
		)
	  }
	  WSpacer()
	  val file = LocalResourceDir.current
	  Button(
		text = state.primaryAction,
		onClick = { send(HowToInvoiceCommand.Next(file)) },
	  )
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