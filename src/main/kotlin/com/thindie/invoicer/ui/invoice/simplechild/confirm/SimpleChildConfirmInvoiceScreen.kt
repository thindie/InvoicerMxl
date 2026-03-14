package com.thindie.invoicer.ui.invoice.simplechild.confirm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*
import javax.swing.JFileChooser

@Composable
fun ScreenScope<SimpleChildConfirmInvoiceState, SimpleChildConfirmInvoiceCommand>.SimpleChildConfirmInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Выбери путь для сохранения",
		onBack = { send(SimpleChildConfirmInvoiceCommand.Back) },
		onClose = { send(SimpleChildConfirmInvoiceCommand.Finish) },
	  )
	  val destination = state.value.destination
	  val text = if (destination == null) {
		"Куда сохраним?"
	  } else {
		"Выбран! : ${destination.path}"
	  }
	  val subtitle = if (destination == null) {
		"если нужно изменить целевой рейтинг - вернись назад"
	  } else {
		null
	  }
	  val icon = if (destination == null) Icons.Default.Edit else Icons.Outlined.Done
	  val border = if (destination == null) BorderStroke(
		color = InvoicerTheme.colors.errorPrimary, width = 1.2.dp
	  ) else BorderStroke(
		color = InvoicerTheme.colors.successPrimary, width = 1.2.dp
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
		  val title = "Открой рейтинг продаж"
		  send(
			SimpleChildConfirmInvoiceCommand.OpenSource(
			  chooser = chooser,
			  title = title,
			  type = fileChooserType,
			)
		  )
		}
	  )
	  VSpacer(12.dp)
	  Text(
		text = "Рейтинг:",
		style = InvoicerTheme.typography.labelMedium,
		color = InvoicerTheme.colors.contentSecondary,
	  )
	  Text(
		text = state.value.source.path,
		color = InvoicerTheme.colors.successPrimary,
		style = InvoicerTheme.typography.bodyMedium,
	  )
	  WSpacer()
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		enabled = destination != null,
		onClick = {
		  send(
			SimpleChildConfirmInvoiceCommand.Done(
			  source = requireNotNull(state.value.source),
			  destination = requireNotNull(state.value.destination)
			)
		  )
		},
		text = "Продолжить"
	  )
	}
  }
}
