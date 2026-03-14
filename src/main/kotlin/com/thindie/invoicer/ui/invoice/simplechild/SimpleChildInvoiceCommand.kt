package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface SimpleChildInvoiceCommand : Command {
  data class Confirm(val source: File) : SimpleChildInvoiceCommand
  data object Back : SimpleChildInvoiceCommand
  data object HowTo : SimpleChildInvoiceCommand
  data object Finish : SimpleChildInvoiceCommand
  data object OpenSource : SimpleChildInvoiceCommand
  data class FilePickDone(val file: File) : SimpleChildInvoiceCommand
  data object FilePickCancelled : SimpleChildInvoiceCommand
}
