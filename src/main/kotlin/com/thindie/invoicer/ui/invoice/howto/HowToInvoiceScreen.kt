package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.Button

@Composable
fun ScreenScope<HowToInvoiceState, HowToInvoiceCommand>.HowToInvoiceScreen(
) {
  Column {
	Button(
	  onClick = { send(HowToInvoiceCommand.Back) },
	  text = "Back"
	)

	Button(
	  onClick = { send(HowToInvoiceCommand.Skip) },
	  text = "Skip"
	)
  }
}
