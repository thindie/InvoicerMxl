package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.ui.invoice.howto.howTo

suspend fun InvoiceFlow.mainExecute(command: InvoiceCommand, state: InvoiceState): InvoiceState {
  return when (command) {
	InvoiceCommand.Back -> {
	  finish(Unit)
	  state
	}
	InvoiceCommand.Main -> {
	  finish(Unit)
	  state
	}

	InvoiceCommand.HowTo -> {
	  go(howTo)
	  state
	}
  }
}
