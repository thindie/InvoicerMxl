package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openFileChooser
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

	SimpleChildInvoiceCommand.HowTo -> {
	  startHowToFlow()
	  state
	}

	is SimpleChildInvoiceCommand.OpenSource -> {
	  val file = try {
		openFileChooser(
		  fileChooserType = command.type,
		  title = command.title,
		  fileChooser = command.chooser
		)
	  } catch (e: Throwable) {
		throw AppError.FileReadError(e.cause, e.message)
	  }
	  state.copy(source = file)
	}
  }
}
