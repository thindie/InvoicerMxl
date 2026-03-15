package com.thindie.invoicer.ui.invoice.merge

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openFileChooser
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.invoice.merge.confirm.mergeBranchesConfirmInvoice


suspend fun InvoiceFlow.simpleInvoiceExecute(
  command: MergeBranchesInvoiceCommand,
  state: MergeBranchesInvoiceState
): MergeBranchesInvoiceState {
  return when (command) {
	MergeBranchesInvoiceCommand.Back -> {
	  back()
	  state
	}

	is MergeBranchesInvoiceCommand.Confirm -> {
	  go(
		mergeBranchesConfirmInvoice(
		  requireNotNull(state.source),
		  requireNotNull(state.branch),
		  )
	  )
	  state
	}

	MergeBranchesInvoiceCommand.Finish -> {
	  finish(InvoiceFlow.Result.Success)
	  state
	}

	MergeBranchesInvoiceCommand.HowTo -> {
	  startHowToFlow()
	  state
	}

	MergeBranchesInvoiceCommand.HowToMakeBranchGreat -> {
	  error("Пока не умеем!")
	  state
	}

	is MergeBranchesInvoiceCommand.OpenSource -> {
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

	is MergeBranchesInvoiceCommand.OpenBranch -> {
	  val file = try {
		openFileChooser(
		  fileChooserType = command.type,
		  title = command.title,
		  fileChooser = command.chooser
		)
	  } catch (e: Throwable) {
		throw AppError.FileReadError(e.cause, e.message)
	  }
	  state.copy(branch = file)
	}
  }
}
