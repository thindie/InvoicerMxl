package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.ui.invoice.InvoiceFlow


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: SimpleChildInvoiceCommand,
  state: SimpleChildInvoiceState
): SimpleChildInvoiceState {
  return when (command) {
	SimpleChildInvoiceCommand.Back -> {
	  back()
	  state
	}
	SimpleChildInvoiceCommand.Confirm -> TODO()
	SimpleChildInvoiceCommand.Finish -> TODO()
	SimpleChildInvoiceCommand.HowTo -> TODO()
  }
}
