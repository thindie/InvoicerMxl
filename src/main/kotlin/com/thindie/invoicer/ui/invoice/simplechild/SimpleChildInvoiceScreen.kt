package com.thindie.invoicer.ui.invoice.simplechild

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.Button

@Composable
fun ScreenScope<SimpleChildInvoiceState, SimpleChildInvoiceCommand>.SimpleChildInvoiceScreen(
) {
  Column {
	Button(
	  onClick = { send(SimpleChildInvoiceCommand.Back) },
	  text = "Back"
	)

	Button(
	  enabled = state.value.source != null && state.value.source != null,
	  onClick = { send(SimpleChildInvoiceCommand.Confirm) },
	  text = "Confirm"
	)
  }
}
