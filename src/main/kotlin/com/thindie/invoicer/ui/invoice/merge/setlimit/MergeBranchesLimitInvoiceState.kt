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
	add(LimitOption("Недельный заказ в ленивом варианте" to 100))
	add(LimitOption("Уже лучше" to 200))
	add(LimitOption("Рассчитываю ничего не потерять" to Int.MAX_VALUE))
  },
  val selected: LimitOption? = null,
) : State

@JvmInline
value class LimitOption(val value: Pair<String, Int>) : State
