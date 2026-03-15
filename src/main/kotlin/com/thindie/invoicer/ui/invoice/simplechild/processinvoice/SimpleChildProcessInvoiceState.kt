package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class SimpleChildProcessInvoiceState(
  val source: File,
  val destination: File,
  val outputPath: String = "",
  val producedFiles: Int = 0,
) : State
