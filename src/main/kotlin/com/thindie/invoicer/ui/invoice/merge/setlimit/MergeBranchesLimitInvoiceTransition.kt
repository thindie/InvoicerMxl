package com.thindie.invoicer.ui.invoice.merge.setlimit

import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.merge.processinvoice.mergeBranchesProcessInvoice


suspend fun InvoiceFlow.mergeBranchesLimitInvoiceExecute(
  command: MergeBranchesLimitInvoiceCommand,
  state: MergeBranchesLimitInvoiceState
): MergeBranchesLimitInvoiceState {
  return when (command) {
	MergeBranchesLimitInvoiceCommand.Back -> {
	  back()
	  state
	}

	is MergeBranchesLimitInvoiceCommand.Done -> {
	  go(
		mergeBranchesProcessInvoice(
		  source = state.source,
		  branch = state.branch,
		  destination = requireNotNull(state.destination),
		  limit = command.limit,
		)
	  )
	  state
	}

	MergeBranchesLimitInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	is MergeBranchesLimitInvoiceCommand.Select -> {
	  state.copy(selected = command.option)
	}
  }
}
