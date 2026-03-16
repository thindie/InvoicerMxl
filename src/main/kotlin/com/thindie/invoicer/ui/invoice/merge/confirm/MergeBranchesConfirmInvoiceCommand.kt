package com.thindie.invoicer.ui.invoice.merge.confirm

import com.thindie.invoicer.application.Command
import java.io.File
import javax.swing.JFileChooser

sealed interface MergeBranchesConfirmInvoiceCommand : Command {
  data class Done(val source: File, val branch: File, val destination: File) : MergeBranchesConfirmInvoiceCommand
  data object Back : MergeBranchesConfirmInvoiceCommand
  data object Finish : MergeBranchesConfirmInvoiceCommand
  data class OpenSource(
	val chooser: JFileChooser,
	val title: String,
	val type: Int
  ) : MergeBranchesConfirmInvoiceCommand
}
