package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.Command

sealed interface HowToInvoiceCommand : Command {
  data object Next : HowToInvoiceCommand
  data object Skip : HowToInvoiceCommand
  data object Back : HowToInvoiceCommand
}
