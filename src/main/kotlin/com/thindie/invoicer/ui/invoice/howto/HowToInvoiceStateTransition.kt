package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.ui.invoice.InvoiceFlow


fun InvoiceFlow.howToExecute(command: HowToInvoiceCommand, state: HowToInvoiceState): HowToInvoiceState {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  back()
	  state
	}

	HowToInvoiceCommand.Next -> {
	  state
	  // todo howto flow
	}

	HowToInvoiceCommand.Skip -> {
	  state
	}
  }
}
