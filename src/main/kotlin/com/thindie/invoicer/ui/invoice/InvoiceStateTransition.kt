package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.ui.invoice.simplechild.simpleChildInvoice
import com.thindie.invoicer.ui.main.Option

fun InvoiceFlow.mainExecute(command: InvoiceCommand, state: InvoiceState): InvoiceState {
  return when (command) {
	InvoiceCommand.Back -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	is InvoiceCommand.Select -> {
	  val toggled = state.options.find { it.id == command.id }
	  when (toggled) {
		is Option.Invoice -> {
		  go(simpleChildInvoice)
		  state
		}

		else -> {
		  state
		}
	  }
	}
  }
}
