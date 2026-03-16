package com.thindie.invoicer.ui.invoice.merge

import com.thindie.invoicer.application.Command
import java.io.File
import javax.swing.JFileChooser

sealed interface MergeBranchesInvoiceCommand : Command {
  data class Confirm(val source: File, val branch: File) : MergeBranchesInvoiceCommand
  data object Back : MergeBranchesInvoiceCommand
  data object HowTo : MergeBranchesInvoiceCommand
  data object HowToMakeBranchGreat : MergeBranchesInvoiceCommand
  data object Finish : MergeBranchesInvoiceCommand
  data class OpenSource(
    val chooser: JFileChooser,
    val title: String,
    val type: Int
  ) : MergeBranchesInvoiceCommand

  data class OpenBranch(
    val chooser: JFileChooser,
    val title: String,
    val type: Int
  ) : MergeBranchesInvoiceCommand
}
