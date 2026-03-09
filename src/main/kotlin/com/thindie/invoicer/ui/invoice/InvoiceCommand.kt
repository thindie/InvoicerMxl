package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.application.Command

sealed interface InvoiceCommand : Command {
  data object Main : InvoiceCommand
  data object Back : InvoiceCommand
  data object HowTo : InvoiceCommand
}
