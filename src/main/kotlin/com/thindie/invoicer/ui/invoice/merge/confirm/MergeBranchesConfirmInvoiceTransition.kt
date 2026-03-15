package com.thindie.invoicer.ui.invoice.merge.confirm

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openFileChooser
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.merge.setlimit.mergeBranchesLimitInvoice


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: MergeBranchesConfirmInvoiceCommand,
  state: MergeBranchesConfirmInvoiceState
): MergeBranchesConfirmInvoiceState {
  return when (command) {
	MergeBranchesConfirmInvoiceCommand.Back -> {
	  back()
	  state
	}

	is MergeBranchesConfirmInvoiceCommand.Done -> {
	  go(
		mergeBranchesLimitInvoice(
		  state.source,
		  state.branch,
		  requireNotNull(state.destination)
		)
	  )
	  state
	}

	MergeBranchesConfirmInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	is MergeBranchesConfirmInvoiceCommand.OpenSource -> {
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
