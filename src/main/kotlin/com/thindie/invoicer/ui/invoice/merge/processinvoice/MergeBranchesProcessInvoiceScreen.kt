package com.thindie.invoicer.ui.invoice.merge.processinvoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*

@Composable
fun ScreenScope<MergeBranchesProcessInvoiceState, MergeBranchesProcessInvoiceCommand>.MergeBranchesProcessInvoiceScreen(
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
	  state.value.summary.forEach { summary ->
		SentenceRow(
		  modifier = Modifier
			.fillMaxWidth()
			.border(
			  BorderStroke(1.dp, InvoicerTheme.colors.backgroundSecondary),
			  shape = RoundedCornerShape(20.dp),
			),
		  painter = if (loading) null else if (state.value.producedFiles == 0) {
			rememberVectorPainter(image = Icons.Filled.Warning)
		  } else rememberVectorPainter(image = Icons.Filled.FavoriteBorder),
		  title = if (loading) "Гружу" else summary.name,
		  subtitle = if (loading) "" else summary.value,
		  loading = loading,
		  enabled = false,
		  onClick = null
		)
		VSpacer(12.dp)
	  }
	  WSpacer()
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		onClick = {
		  send(
			MergeBranchesProcessInvoiceCommand.Finish
		  )
		},
		text = "Завершить работу"
	  )
	  VSpacer(16.dp)
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		onClick = {
		  send(
			MergeBranchesProcessInvoiceCommand.Back
		  )
		},
		text = "Закончить"
	  )
	}
  }
}
