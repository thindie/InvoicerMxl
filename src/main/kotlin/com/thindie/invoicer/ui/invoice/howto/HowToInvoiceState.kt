package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State

@Immutable
data class HowToInvoiceState(
  val showChooser: Boolean = false,
): State
