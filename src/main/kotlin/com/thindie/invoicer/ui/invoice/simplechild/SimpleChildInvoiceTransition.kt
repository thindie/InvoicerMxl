package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.simplechild.confirm.simpleChildConfirmInvoice


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: SimpleChildInvoiceCommand,
  state: SimpleChildInvoiceState
): SimpleChildInvoiceState {
  return when (command) {
	SimpleChildInvoiceCommand.Back -> {
	  back()
	  state
	}

	is SimpleChildInvoiceCommand.Confirm -> {
	  go(simpleChildConfirmInvoice(requireNotNull(state.source)))
	  state
	}

	SimpleChildInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	SimpleChildInvoiceCommand.HowTo -> TODO()
	SimpleChildInvoiceCommand.OpenSource -> {
	  state.copy(
		showChooser = true
	  )
	}

	is SimpleChildInvoiceCommand.FilePickDone -> {
	  state.copy(
		showChooser = false,
		source = command.file,
	  )
	}

	is SimpleChildInvoiceCommand.FilePickCancelled -> {
	  state.copy(
		showChooser = false,
		source = null,
	  )
	}
  }
}
