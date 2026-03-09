package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.Button
import com.thindie.invoicer.application.uikit.FileChooserEffect
import javax.swing.JFileChooser

@Composable
fun ScreenScope<HowToInvoiceState, HowToInvoiceCommand>.HowToInvoiceScreen(
) {
  Column {
	Button(
	  onClick = { send(HowToInvoiceCommand.Back) },
	  text = "Back"
	)

	Button(
	  onClick = { send(HowToInvoiceCommand.Finish) },
	  text = "Finish"
	)

	Button(
	  onClick = { send(HowToInvoiceCommand.ChooseFile) },
	  text = "Choose a file"
	)
	if (state.value.showChooser) {
	  FileChooserEffect(
		title = JFileChooser.SAVE_DIALOG.toString(),
		path = "",
		fileChooserType = JFileChooser.APPROVE_OPTION,
		onResult = { file -> file?.let { send(HowToInvoiceCommand.FileChosen(it)) } }
	  )
	}
  }
}
