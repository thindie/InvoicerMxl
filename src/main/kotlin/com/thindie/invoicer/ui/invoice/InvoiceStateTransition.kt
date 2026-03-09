package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.ui.invoice.simplechild.simpleChildInvoice

fun InvoiceFlow.mainExecute(command: InvoiceCommand, state: InvoiceState): InvoiceState {
  return when (command) {
	InvoiceCommand.Back -> {
	  finish(Unit)
	  state
	}
	InvoiceCommand.Main -> {
	  finish(Unit)
	  state
	}

	InvoiceCommand.MergeChildMotherInvoice -> {
	  state
	  // todo
	}

	InvoiceCommand.SingleChildInvoice -> {
	  go(simpleChildInvoice)
	  state
	}
  }
}
