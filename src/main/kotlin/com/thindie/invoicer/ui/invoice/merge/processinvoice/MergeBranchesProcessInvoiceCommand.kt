package com.thindie.invoicer.ui.invoice.merge.processinvoice

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface MergeBranchesProcessInvoiceCommand : Command {
  data object Finish : MergeBranchesProcessInvoiceCommand
  data object Back : MergeBranchesProcessInvoiceCommand
  data class Process(
    val source: File,
    val branch: File,
    val target: File,
    val limit: Int,
    val offset: Int,
  ): MergeBranchesProcessInvoiceCommand
}
