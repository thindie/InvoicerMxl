package com.thindie.invoicer.ui.invoice.merge.processinvoice

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State
import java.io.File

@Immutable
data class MergeBranchesProcessInvoiceState(
  val source: File,
  val branch: File,
  val destination: File,
  val limit: Int?,
  val offset: Int,
  val outputPath: String = "",
  val producedFiles: Int = 0,
) : State

@JvmInline
value class Summary(val item: Pair<String, Any>) {
  val name: String get() = item.first
  val value: String get() = item.second.toString()
}

val MergeBranchesProcessInvoiceState.summary
  get() = buildSet {
    if (producedFiles > 0) {
      add(Summary("Объем" to "${producedFiles} файлов по 100 строк"))
      add(Summary("Директория" to File(outputPath).parent))
      add(Summary("Настройки" to "Лимит позиций: ${limit ?: "∞"}"))
      add(Summary("Настройки" to "Offset прочтения: $offset"))
    } else {
      add(Summary("Заказа нет!" to "Работа прошла без ошибок, однако итоговый результат пустой: или наличие в порядке, или что-то с установками"))
      add(Summary("Настройки" to "Лимит позиций: ${limit ?: "∞"}"))
      add(Summary("Настройки" to "Offset прочтения: $offset"))
    }
  }
