package com.thindie.invoicer.ui.invoice.simplechild.confirm

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface SimpleChildConfirmInvoiceCommand : Command {
  data class Done(val source: File, val destination: File) : SimpleChildConfirmInvoiceCommand
  data object Back : SimpleChildConfirmInvoiceCommand
  data object Finish : SimpleChildConfirmInvoiceCommand
  data object OpenSource : SimpleChildConfirmInvoiceCommand
  data class FilePickDone(val file: File) : SimpleChildConfirmInvoiceCommand
  data object FilePickCancelled : SimpleChildConfirmInvoiceCommand
}
