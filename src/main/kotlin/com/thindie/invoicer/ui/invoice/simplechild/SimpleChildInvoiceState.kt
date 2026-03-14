package com.thindie.invoicer.ui.invoice.simplechild

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class SimpleChildInvoiceState(
  val source: File? = null,
) : State
