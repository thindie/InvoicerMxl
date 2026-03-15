package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*

@Composable
fun ScreenScope<SimpleChildProcessInvoiceState, SimpleChildProcessInvoiceCommand>.SimpleChildProcessInvoiceScreen(
) {
  val screenScope = this
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(title = "Готово!")
	  WSpacer()
	  val invoiceProcessed = screenScope.processing.value
	  val loading = invoiceProcessed != null
	  SentenceRow(
		modifier = Modifier
		  .fillMaxWidth()
		  .border(
		  BorderStroke(1.dp, InvoicerTheme.colors.backgroundSecondary),
		  shape = RoundedCornerShape(20.dp),
		),
		painter = if (loading) null else rememberVectorPainter(image = Icons.Filled.FavoriteBorder),
		title = if (loading) "Гружу" else "Заверешно!",
		subtitle = if (loading) "" else "Всего записали файлов: ${state.value.producedFiles}",
		loading = loading,
		enabled = false,
		onClick = null
	  )
	  VSpacer(12.dp)
	  SentenceRow(
		modifier = Modifier
		  .fillMaxWidth()
		  .border(
		  BorderStroke(1.dp, InvoicerTheme.colors.backgroundSecondary),
		  shape = RoundedCornerShape(20.dp),
		),
		painter = if (loading) null else rememberVectorPainter(image = Icons.Filled.CheckCircle),
		title = "",
		subtitle = if (loading) "Гружу" else "Адрес: ${state.value.outputPath}",
		loading = loading,
		enabled = false,
		onClick = null
	  )
	  WSpacer()
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		onClick = {
		  send(
			SimpleChildProcessInvoiceCommand.Finish
		  )
		},
		text = "Завершить работу"
	  )
	  VSpacer(16.dp)
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		onClick = {
		  send(
			SimpleChildProcessInvoiceCommand.Back
		  )
		},
		text = "Закончить"
	  )
	}
  }
}
