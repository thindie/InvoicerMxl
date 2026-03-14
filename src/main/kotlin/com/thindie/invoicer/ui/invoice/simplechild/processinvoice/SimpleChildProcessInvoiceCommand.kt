package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface SimpleChildProcessInvoiceCommand : Command {
  data object Finish : SimpleChildProcessInvoiceCommand
  data object Back : SimpleChildProcessInvoiceCommand
}
