package com.thindie.invoicer.ui.invoice.merge.setoffset

import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.merge.processinvoice.mergeBranchesProcessInvoice


suspend fun InvoiceFlow.mergeBranchesOffsetInvoiceExecute(
  command: MergeBranchesOffsetInvoiceCommand,
  state: MergeBranchesOffsetInvoiceState
): MergeBranchesOffsetInvoiceState {
  return when (command) {
	MergeBranchesOffsetInvoiceCommand.Back -> {
	  back()
	  state
	}

	is MergeBranchesOffsetInvoiceCommand.Done -> {
	  go(
		mergeBranchesProcessInvoice(
		  source = state.source,
		  branch = state.branch,
		  destination = requireNotNull(state.destination),
		  limit = command.limit,
		  offset = command.offset,
		)
	  )
	  state
	}

	MergeBranchesOffsetInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	is MergeBranchesOffsetInvoiceCommand.Select -> {
	  state.copy(selected = command.option)
	}
  }
}
