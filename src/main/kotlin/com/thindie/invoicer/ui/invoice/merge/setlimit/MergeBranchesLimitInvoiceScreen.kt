package com.thindie.invoicer.ui.invoice.merge.setlimit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*

@Composable
fun ScreenScope<MergeBranchesLimitInvoiceState, MergeBranchesLimitInvoiceCommand>.MergeBranchesLimitInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Финальная настройка",
		onBack = { send(MergeBranchesLimitInvoiceCommand.Back) },
		onClose = { send(MergeBranchesLimitInvoiceCommand.Finish) },
	  )
	  WSpacer()
	  state.value.options.forEach { option ->
		val selected = option.value.first == state.value.selected?.value?.first
		val border = if (selected) {
		  BorderStroke(1.dp, InvoicerTheme.colors.successPrimary)
		} else BorderStroke(1.2.dp, InvoicerTheme.colors.backgroundSecondary)

		val limit = option.value.second
		val title = remember(option.value.first, option.value.second) {
		  if (limit == Int.MAX_VALUE) {
			"Все сразу"
		  } else {
			limit.toString() + " Позиций"
		  }
		}
		SentenceRow(
		  modifier = Modifier
			.border(border, RoundedCornerShape(20.dp))
			.fillMaxWidth(),
		  title = title,
		  subtitle = "лимит",
		  painter = rememberVectorPainter(image = Icons.Default.Favorite).takeIf { selected },
		  loading = false,
		  enabled = !selected,
		  onClick = {
			send(MergeBranchesLimitInvoiceCommand.Select(option))
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
		subtitle = "рейтинг может быть огромным, можем установить лимит позиций",
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
			MergeBranchesLimitInvoiceCommand.Done(
			  source = requireNotNull(state.value.source),
			  destination = requireNotNull(state.value.destination),
			  branch = requireNotNull(state.value.branch),
			  limit = requireNotNull(state.value.selected?.value?.second)
			)
		  )
		},
		text = "Продолжить"
	  )
	}
  }
}
