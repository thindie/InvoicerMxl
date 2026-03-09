package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.Command

sealed interface SimpleChildInvoiceCommand : Command {
  data object Confirm : SimpleChildInvoiceCommand
  data object Back : SimpleChildInvoiceCommand
  data object HowTo : SimpleChildInvoiceCommand
  data object Finish : SimpleChildInvoiceCommand
}
