package com.thindie.invoicer.ui.invoice.simplechild.confirm

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openFileChooser
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
	  go(simpleChildProcessInvoice(state.source, requireNotNull(state.destination)))
	  state
	}
	SimpleChildConfirmInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	is SimpleChildConfirmInvoiceCommand.OpenSource -> {
	  val file = try {
		openFileChooser(
		  fileChooserType = command.type,
		  title = command.title,
		  fileChooser = command.chooser
		)
	  } catch (e: Throwable) {
		throw AppError.FileReadError(e.cause, e.message)
	  }
	  state.copy(destination = file)
	}
  }
}
