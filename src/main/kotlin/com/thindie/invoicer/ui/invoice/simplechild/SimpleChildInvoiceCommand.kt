package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.Command
import java.io.File
import javax.swing.JFileChooser

sealed interface SimpleChildInvoiceCommand : Command {
  data class Confirm(val source: File) : SimpleChildInvoiceCommand
  data object Back : SimpleChildInvoiceCommand
  data object HowTo : SimpleChildInvoiceCommand
  data object Finish : SimpleChildInvoiceCommand
  data class OpenSource(
    val chooser: JFileChooser,
    val title: String,
    val type: Int
  ) : SimpleChildInvoiceCommand
}
