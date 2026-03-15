package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.application.Command

sealed interface InvoiceCommand : Command {
  data object Back : InvoiceCommand
  data class Select(val id: String) : InvoiceCommand
}
