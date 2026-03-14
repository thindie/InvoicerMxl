package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.Command
import java.io.File
import javax.swing.JFileChooser

sealed interface HowToInvoiceCommand : Command {
  data class Next(val dir: File) : HowToInvoiceCommand
  data object Back : HowToInvoiceCommand
  data class RequestRatingHelper(
    val fileChooser: JFileChooser,
    val resDir: File
  ) : HowToInvoiceCommand
}
