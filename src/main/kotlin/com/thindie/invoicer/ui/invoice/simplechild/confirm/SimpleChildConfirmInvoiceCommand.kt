package com.thindie.invoicer.ui.invoice.simplechild.confirm

import com.thindie.invoicer.application.Command
import java.io.File
import javax.swing.JFileChooser

sealed interface SimpleChildConfirmInvoiceCommand : Command {
  data class Done(val source: File, val destination: File) : SimpleChildConfirmInvoiceCommand
  data object Back : SimpleChildConfirmInvoiceCommand
  data object Finish : SimpleChildConfirmInvoiceCommand
  data class OpenSource(
	val chooser: JFileChooser,
	val title: String,
	val type: Int
  ) : SimpleChildConfirmInvoiceCommand
}
