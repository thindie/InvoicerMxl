package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import com.thindie.invoicer.ui.invoice.InvoiceFlow


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: SimpleChildProcessInvoiceCommand,
  state: SimpleChildProcessInvoiceState
): SimpleChildProcessInvoiceState {
  return when (command) {
	SimpleChildProcessInvoiceCommand.Back -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}
	SimpleChildProcessInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Finish)
	  state
	}
	is SimpleChildProcessInvoiceCommand.Process -> {
	  val result = repository.writeSimpleChildInvoice(
		inputPath = command.source.path,
		outputPath = command.target.path
	  )
	  state.copy(
		producedFiles = result.filesProduced,
		outputPath = result.outputPath
	  )
	}
  }
}
