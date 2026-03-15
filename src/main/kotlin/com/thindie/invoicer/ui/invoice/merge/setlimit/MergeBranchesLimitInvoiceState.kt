package com.thindie.invoicer.ui.invoice.merge.setlimit

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class MergeBranchesLimitInvoiceState(
  val source: File,
  val branch: File,
  val destination: File,
  val options: List<LimitOption> = buildList {
	add(LimitOption("1" to 100))
	add(LimitOption("2" to 200))
	add(LimitOption("3" to Int.MAX_VALUE))
  },
  val selected: LimitOption? = null,
) : State

@JvmInline
value class LimitOption(val value: Pair<String, Int>) : State
