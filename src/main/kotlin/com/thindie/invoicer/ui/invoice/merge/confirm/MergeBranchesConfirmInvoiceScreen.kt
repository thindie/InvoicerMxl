package com.thindie.invoicer.ui.invoice.merge.confirm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*
import javax.swing.JFileChooser

@Composable
fun ScreenScope<MergeBranchesConfirmInvoiceState, MergeBranchesConfirmInvoiceCommand>.MergeBranchesConfirmInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Выбери путь для сохранения",
		onBack = { send(MergeBranchesConfirmInvoiceCommand.Back) },
		onClose = { send(MergeBranchesConfirmInvoiceCommand.Finish) },
	  )
	  val destination = state.value.destination
	  val text = if (destination == null) {
		"Куда сохраним наш заказ из выбранного рейтинга?"
	  } else {
		"Я буду тут!"
	  }
	  val subtitle = if (destination == null) {
		"если нужно изменить целевой рейтинг или инвертаризацию филиала - вернись назад"
	  } else {
		destination.path
	  }
	  val icon = if (destination == null) Icons.Default.Edit else Icons.Outlined.Create
	  val border = if (destination == null) BorderStroke(
		color = InvoicerTheme.colors.errorPrimary, width = 1.dp
	  ) else BorderStroke(
		color = InvoicerTheme.colors.successPrimary, width = 1.dp
	  )
	  WSpacer()
	  val chooser = LocalFileChooser.current
	  SentenceRow(
		modifier = Modifier
		  .border(border, RoundedCornerShape(20.dp))
		  .fillMaxWidth(),
		title = text,
		subtitle = subtitle,
		painter = rememberVectorPainter(image = icon),
		loading = false,
		onClick = {
		  val fileChooserType = JFileChooser.SAVE_DIALOG
		  val title = "Выбери путь сохранения выгрузки"
		  send(
			MergeBranchesConfirmInvoiceCommand.OpenSource(
			  chooser = chooser,
			  title = title,
			  type = fileChooserType,
			)
		  )
		}
	  )
	  VSpacer(12.dp)
	  SentenceRow(
		modifier = Modifier
		  .border(
			BorderStroke(
			  color = InvoicerTheme.colors.backgroundSecondary, width = 1.2.dp
			),
			shape = RoundedCornerShape(20.dp)
		  )
		  .fillMaxWidth(),
		title = "Выбран рейтинг продаж:",
		subtitle = state.value.source.path,
		painter = rememberVectorPainter(image = Icons.Default.Done),
		loading = false,
		onClick = null,
		enabled = false
	  )
	  VSpacer(12.dp)
	  SentenceRow(
		modifier = Modifier
		  .border(
			BorderStroke(
			  color = InvoicerTheme.colors.backgroundSecondary, width = 1.2.dp
			),
			shape = RoundedCornerShape(20.dp)
		  )
		  .fillMaxWidth(),
		title = "Подтянута инвентаризация:",
		subtitle = state.value.branch.path,
		painter = rememberVectorPainter(image = Icons.Default.Done),
		loading = false,
		onClick = null,
		enabled = false
	  )
	  WSpacer()
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		enabled = destination != null,
		onClick = {
		  send(
			MergeBranchesConfirmInvoiceCommand.Done(
			  source = requireNotNull(state.value.source),
			  destination = requireNotNull(state.value.destination),
			  branch = requireNotNull(state.value.branch)
			)
		  )
		},
		text = "Продолжить"
	  )
	}
  }
}
