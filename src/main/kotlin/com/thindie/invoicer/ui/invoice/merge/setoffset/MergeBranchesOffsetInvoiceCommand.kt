package com.thindie.invoicer.ui.invoice.merge.setoffset

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface MergeBranchesOffsetInvoiceCommand : Command {
  data class Done(
	val source: File,
	val branch: File,
	val destination: File,
	val limit: Int,
	val offset: Int
  ) : MergeBranchesOffsetInvoiceCommand

  data object Back : MergeBranchesOffsetInvoiceCommand
  data object Finish : MergeBranchesOffsetInvoiceCommand
  data class Select(val option: OffsetOption) : MergeBranchesOffsetInvoiceCommand
}
