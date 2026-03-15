package com.thindie.invoicer.ui.invoice.merge.setoffset

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class MergeBranchesOffsetInvoiceState(
  val source: File,
  val branch: File,
  val destination: File,
  val limit: Int?,
  val options: List<OffsetOption> = buildList {
	add(OffsetOption("Отсечем самую мелочь" to 500 and "Фильтры, свечи"))
	add(OffsetOption("Подвесочка" to 1000 and "Видимо, тормоза и амортизаторы"))
	add(OffsetOption("Я скурпулезен" to 0 and "Удачи с флянцами и шайбами!"))
  },
  val selected: OffsetOption? = null,
) : State

@JvmInline
value class OffsetOption(val value: Triple<String, Int, String>) : State

private infix fun <A, B, C> Pair<A, B>.and(c: C) = Triple(this.first, this.second, c)
