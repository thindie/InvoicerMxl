package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import com.thindie.invoicer.ui.invoice.InvoiceFlow


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: SimpleChildProcessInvoiceCommand,
  state: SimpleChildProcessInvoiceState
): SimpleChildProcessInvoiceState {
  when (command) {
	SimpleChildProcessInvoiceCommand.Back -> finish(InvoiceFlow.Result.Success)
	SimpleChildProcessInvoiceCommand.Finish -> finish(InvoiceFlow.Result.Finish)
  }
  return state
}
