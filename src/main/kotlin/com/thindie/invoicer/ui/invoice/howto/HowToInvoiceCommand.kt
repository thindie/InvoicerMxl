package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.Command
import java.io.File

sealed interface HowToInvoiceCommand : Command {
  data object Next : HowToInvoiceCommand
  data object Skip : HowToInvoiceCommand
  data object Back : HowToInvoiceCommand
  data object Finish : HowToInvoiceCommand
  data object ChooseFile : HowToInvoiceCommand
  data class FileChosen(val file: File) : HowToInvoiceCommand
  data object CloseChooser : HowToInvoiceCommand
}
