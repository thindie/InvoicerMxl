package com.thindie.invoicer.ui.invoice.merge.processinvoice

import com.thindie.invoicer.ui.invoice.InvoiceFlow


suspend fun InvoiceFlow.mergeInvoiceProcessExecute(
  command: MergeBranchesProcessInvoiceCommand,
  state: MergeBranchesProcessInvoiceState
): MergeBranchesProcessInvoiceState {
  return when (command) {
	MergeBranchesProcessInvoiceCommand.Back -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	MergeBranchesProcessInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Finish)
	  state
	}

	is MergeBranchesProcessInvoiceCommand.Process -> {
	  val result = repository.writeMergeBranchInvoice(
		inputPathParent = command.source.path,
		inputPathChild = command.branch.path,
		outputPath = command.target.path,
		limit = command.limit.takeIf { it != Int.MAX_VALUE },
		offset = command.offset
	  )
	  state.copy(
		producedFiles = result.filesProduced,
		outputPath = result.outputPath
	  )
	}
  }
}
