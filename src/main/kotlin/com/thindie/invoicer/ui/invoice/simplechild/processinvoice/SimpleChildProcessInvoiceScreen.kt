package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.AppScreen
import com.thindie.invoicer.application.uikit.Button
import com.thindie.invoicer.application.uikit.TopAppBar
import com.thindie.invoicer.application.uikit.VSpacer
import com.thindie.invoicer.application.uikit.WSpacer

@Composable
fun ScreenScope<SimpleChildProcessInvoiceState, SimpleChildProcessInvoiceCommand>.SimpleChildProcessInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(title = "Готово!")
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
