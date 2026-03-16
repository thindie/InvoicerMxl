package com.thindie.invoicer.ui.invoice.merge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun ScreenScope<MergeBranchesInvoiceState, MergeBranchesInvoiceCommand>.MergeBranchesInvoiceScreen(
) {
  AppScreen {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.padding(16.dp),
	) {
	  TopAppBar(
		title = "Попробуем поднять потенциал продаж",
		onBack = { send(MergeBranchesInvoiceCommand.Back) },
		onClose = { send(MergeBranchesInvoiceCommand.Finish) },
	  )
	  val source = state.value.source
	  val text = if (source == null) {
		"Здесь нужен рейтинг продаж другого магазина"
	  } else {
		"Выбран! : ${source.path}"
	  }
	  val subtitle = if (source == null) {
		"для того чтобы продолжить"
	  } else {
		null
	  }

	  val branch = state.value.branch
	  val textBranch = if (branch == null) {
		"Здесь нужна инвентаризация филиала"
	  } else {
		"Выбран! : ${branch.path}"
	  }
	  val subtitleBranch = if (source == null) {
		"который хотим качнуть"
	  } else {
		null
	  }


	  val icon = if (source == null) Icons.Outlined.Warning else Icons.Outlined.Done
	  val branchIcon = if (branch == null) Icons.Outlined.Warning else Icons.Outlined.Done

	  val border = if (source == null) BorderStroke(
		color = InvoicerTheme.colors.errorPrimary, width = 1.dp
	  ) else BorderStroke(
		color = InvoicerTheme.colors.successPrimary, width = 1.dp
	  )
	  val branchBorder = if (branch == null) BorderStroke(
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
		  val title = "Выбери место сохранения"
		  send(
			MergeBranchesInvoiceCommand.OpenSource(
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
		  .border(branchBorder, RoundedCornerShape(20.dp))
		  .fillMaxWidth(),
		title = textBranch,
		subtitle = subtitleBranch,
		painter = rememberVectorPainter(image = branchIcon),
		loading = false,
		onClick = {
		  val fileChooserType = JFileChooser.SAVE_DIALOG
		  val title = "Выбери место сохранения"
		  send(
			MergeBranchesInvoiceCommand.OpenBranch(
			  chooser = chooser,
			  title = title,
			  type = fileChooserType,
			)
		  )
		}
	  )
	  VSpacer(12.dp)
	  Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
	  ) {
		Text(
		  modifier = Modifier
			.clickable(
			  indication = null,
			  interactionSource = remember { MutableInteractionSource() },
			  onClick = { send(MergeBranchesInvoiceCommand.HowTo) }
			)
			.weight(1f),
		  text = "Вспомнить, как сделать рейтинг продаж",
		  textAlign = TextAlign.Center,
		  color = InvoicerTheme.colors.accentPrimary,
		  style = InvoicerTheme.typography.labelLarge
		)

		Text(
		  modifier = Modifier
			.clickable(
			  indication = null,
			  interactionSource = remember { MutableInteractionSource() },
			  onClick = { send(MergeBranchesInvoiceCommand.HowToMakeBranchGreat) }
			)
			.weight(1f),
		  text = "Узнать, как добыть продажи другого филиала",
		  textAlign = TextAlign.Center,
		  color = InvoicerTheme.colors.accentPrimary,
		  style = InvoicerTheme.typography.labelLarge
		)
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
		subtitle = "попытаемся взять позиции, которые или выпали, или мы упустили при наполнении склада, воспользовавшись успехами коллег",
		painter = rememberVectorPainter(image = Icons.Default.Search),
		loading = false,
		onClick = null,
		enabled = false
	  )
	  VSpacer(12.dp)
	  Button(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		enabled = source != null,
		onClick = {
		  send(
			MergeBranchesInvoiceCommand.Confirm(
			  source = requireNotNull(source),
			  branch = requireNotNull(branch)
			)
		  )
		},
		text = "Продолжить"
	  )
	}
  }
}
