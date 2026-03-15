package com.thindie.invoicer.ui.invoice.merge.setoffset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*

@Composable
fun ScreenScope<MergeBranchesOffsetInvoiceState, MergeBranchesOffsetInvoiceCommand>.MergeBranchesOffsetInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Финальная настройка",
		description = "Отступ от верхних позиций",
		onBack = { send(MergeBranchesOffsetInvoiceCommand.Back) },
		onClose = { send(MergeBranchesOffsetInvoiceCommand.Finish) },
	  )
	  WSpacer()
	  SentenceRow(
		modifier = Modifier
		  .fillMaxWidth(),
		title = "Обрати внимание на нюанс: не отодвинься глубже, чем рейтинг продаж объекта подражания.",
		subtitle = "И ты уже выбрал лимит: ${state.value.limit.takeIf { it != Int.MAX_VALUE }?.or(0)}",
		painter = rememberVectorPainter(image = Icons.Default.Info),
		loading = false,
		enabled = false,
		onClick = null
	  )
	  WSpacer()
	  state.value.options.forEach { option ->
		val selected = option.value.first == state.value.selected?.value?.first
		val border = if (selected) {
		  BorderStroke(1.dp, InvoicerTheme.colors.successPrimary)
		} else BorderStroke(1.2.dp, InvoicerTheme.colors.backgroundSecondary)
		val (title, offset, subtitle) = option.value
		SentenceRow(
		  modifier = Modifier
			.border(border, RoundedCornerShape(20.dp))
			.fillMaxWidth(),
		  title = title,
		  subtitle = "Отступ от топа: ${offset.takeIf { it != Int.MAX_VALUE } ?: "Беру все."}. Товары, которые ожидаем: $subtitle",
		  painter = rememberVectorPainter(image = Icons.Default.Favorite).takeIf { selected },
		  loading = false,
		  enabled = !selected,
		  onClick = {
			send(MergeBranchesOffsetInvoiceCommand.Select(option))
		  }
		)
		VSpacer(16.dp)
	  }
	  WSpacer()
	  SentenceRow(
		modifier = Modifier
		  .border(
			BorderStroke(color = InvoicerTheme.colors.backgroundSecondary, width = 1.dp),
			RoundedCornerShape(20.dp)
		  )
		  .fillMaxWidth(),
		title = "Что тут вообще происходит?",
		subtitle = "Часто хочется срезать пену и взять вещи потяжелей",
		painter = rememberVectorPainter(image = Icons.Default.Search),
		loading = false,
		onClick = null,
		enabled = false
	  )
	  VSpacer(12.dp)
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		enabled = state.value.selected != null,
		onClick = {
		  send(
			MergeBranchesOffsetInvoiceCommand.Done(
			  source = requireNotNull(state.value.source),
			  destination = requireNotNull(state.value.destination),
			  branch = requireNotNull(state.value.branch),
			  limit = requireNotNull(state.value.limit),
			  offset = requireNotNull(state.value.selected?.value?.second)
			)
		  )
		},
		text = "Продолжить"
	  )
	}
  }
}
