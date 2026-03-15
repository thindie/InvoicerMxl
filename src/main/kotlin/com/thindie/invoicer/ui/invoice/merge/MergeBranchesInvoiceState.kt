package com.thindie.invoicer.ui.invoice.merge

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class MergeBranchesInvoiceState(
  val source: File? = null,
  val branch: File? = null,
) : State
