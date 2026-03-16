package com.thindie.invoicer.ui.invoice.merge.confirm

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class MergeBranchesConfirmInvoiceState(
  val source: File,
  val branch: File,
  val destination: File? = null,
): State
