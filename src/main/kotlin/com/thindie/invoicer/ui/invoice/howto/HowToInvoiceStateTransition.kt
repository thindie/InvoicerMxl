package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.ui.invoice.InvoiceFlow


suspend fun InvoiceFlow.howToExecute(command: HowToInvoiceCommand, state: HowToInvoiceState): HowToInvoiceState {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  back()
	  state
	}

	HowToInvoiceCommand.Next -> {
	  state
	}

	HowToInvoiceCommand.Skip -> {
	  state
	}

	HowToInvoiceCommand.Finish -> {
	  finish(Unit)
	  state
	}

	is HowToInvoiceCommand.FileChosen -> {
	  state.copy(showChooser = false)
	}

	HowToInvoiceCommand.ChooseFile,
	HowToInvoiceCommand.CloseChooser -> {
	  state.copy(showChooser = true)
	}
  }
}
