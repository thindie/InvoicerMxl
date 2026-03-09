package com.thindie.invoicer.ui.invoice

import androidx.compose.runtime.Stable
import com.thindie.invoicer.application.State

@Stable
data class InvoiceState(val invoiceMarker: String = "invoice!", val quota: Int = 0) : State
