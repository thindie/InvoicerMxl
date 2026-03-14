package com.thindie.invoicer.ui.invoice.simplechild.confirm

import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.simplechild.processinvoice.simpleChildProcessInvoice


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: SimpleChildConfirmInvoiceCommand,
  state: SimpleChildConfirmInvoiceState
): SimpleChildConfirmInvoiceState {
  return when (command) {
	SimpleChildConfirmInvoiceCommand.Back -> {
	  back()
	  state
	}
	is SimpleChildConfirmInvoiceCommand.Done -> {
	  go(simpleChildProcessInvoice())
	  state
	}
	SimpleChildConfirmInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	SimpleChildConfirmInvoiceCommand.OpenSource -> {
	  state.copy(
		showChooser = true
	  )
	}
	is SimpleChildConfirmInvoiceCommand.FilePickDone -> {
	  state.copy(
		showChooser = false,
		destination = command.file,
	  )
	}
	is SimpleChildConfirmInvoiceCommand.FilePickCancelled -> {
	  state.copy(
		showChooser = false,
		destination = null,
	  )
	}
  }
}
