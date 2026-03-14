package com.thindie.invoicer.ui.invoice.simplechild.confirm

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class SimpleChildConfirmInvoiceState(
  val source: File,
  val showChooser: Boolean = false,
  val destination: File? = null,
): State
