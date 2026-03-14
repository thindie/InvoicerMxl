package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.ui.invoice.simplechild.simpleChildInvoice

fun InvoiceFlow.mainExecute(command: InvoiceCommand, state: InvoiceState): InvoiceState {
  return when (command) {
	InvoiceCommand.Back -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	InvoiceCommand.Main -> {
	  finish(InvoiceFlow.Result.Success)
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
