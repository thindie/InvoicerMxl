package com.thindie.invoicer.ui.invoice.simplechild

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.*
import javax.swing.JFileChooser

@Composable
fun ScreenScope<SimpleChildInvoiceState, SimpleChildInvoiceCommand>.SimpleChildInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Выбери файл рейтинга",
		onBack = { send(SimpleChildInvoiceCommand.Back) },
		onClose = { send(SimpleChildInvoiceCommand.Finish) },
	  )
	  val source = state.value.source
	  val text = if (source == null) {
		"Нужно выбрать файл с рейтингом"
	  } else {
		"Выбран! : ${source.path}"
	  }
	  val subtitle = if (source == null) {
		"для того чтобы продолжить"
	  } else {
		null
	  }
	  val icon = if (source == null) Icons.Outlined.Warning else Icons.Outlined.Done
	  val border = if (source == null) BorderStroke(
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
		  val title = "Выбери место сохранения"
		  send(
			SimpleChildInvoiceCommand.OpenSource(
			  chooser = chooser,
			  title = title,
			  type = fileChooserType,
			)
		  )
		}
	  )
	  VSpacer(12.dp)
	  Text(
		modifier = Modifier
		  .clickable(
			indication = null,
			interactionSource = remember { MutableInteractionSource() },
			onClick = { send(SimpleChildInvoiceCommand.HowTo) }
		  )
		  .fillMaxWidth(),
		text = "Как мне это сделать?",
		textAlign = TextAlign.Center,
		color = InvoicerTheme.colors.accentPrimary,
		style = InvoicerTheme.typography.titleSmall
	  )
	  WSpacer()
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		enabled = source != null,
		onClick = { send(SimpleChildInvoiceCommand.Confirm(requireNotNull(source))) },
		text = "Продолжить"
	  )
	}
  }
}
