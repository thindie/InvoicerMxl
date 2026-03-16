package com.thindie.invoicer.ui.invoice.merge.setlimit

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface MergeBranchesLimitInvoiceCommand : Command {
  data class Done(
	val source: File,
	val branch: File,
	val destination: File,
	val limit: Int
  ) : MergeBranchesLimitInvoiceCommand

  data object Back : MergeBranchesLimitInvoiceCommand
  data object Finish : MergeBranchesLimitInvoiceCommand
  data class Select(val option: LimitOption) : MergeBranchesLimitInvoiceCommand
}
