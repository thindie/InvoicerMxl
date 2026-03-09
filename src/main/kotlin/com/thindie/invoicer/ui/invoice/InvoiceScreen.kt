package com.thindie.invoicer.ui.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.thindie.invoicer.application.ScreenScope

@Composable
fun ScreenScope<InvoiceState, InvoiceCommand>.InvoiceScreen(
) {
  Column {
	Button(onClick = { send(InvoiceCommand.Main) }) {
	  Text("Main ${state.value.invoiceMarker}")
	}

	Button(onClick = { send(InvoiceCommand.HowTo) }) {
	  Text("How to Invoice")
	}
  }
}
