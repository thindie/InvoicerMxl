package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.uikit.extractRatingHelper
import com.thindie.invoicer.application.uikit.getImageBitmap
import com.thindie.invoicer.application.uikit.openFileChooser
import javax.swing.JFileChooser

suspend fun HowToFlow.howToIntroExecute(
  command: HowToInvoiceCommand,
  state: HowToIntroState,
): HowToIntroState {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  finish(HowToFlow.Result.Finish)
	  state
	}

	is HowToInvoiceCommand.Next -> {
	  val img = getImageBitmap(requireNotNull(state.imageRes), command.dir)
	  go(howToStep0(img, designation))
	  state
	}

	is HowToInvoiceCommand.RequestRatingHelper -> {
	  val file = openFileChooser(
		fileChooserType = JFileChooser.SAVE_DIALOG,
		title = "Выбери путь, куда сохранить обработку.",
		fileChooser = command.fileChooser,
	  )
	  if (file != null) {
		extractRatingHelper(resdir = command.resDir, resourcePath = "1c_rating_configurator.ert", destination = file)
	  }
	  state
	}
  }
}

suspend fun HowToFlow.howToStep0Execute(
  command: HowToInvoiceCommand,
  state: HowToStep0State,
): HowToStep0State {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  back()
	  state
	}

	is HowToInvoiceCommand.Next -> {
	  val img = getImageBitmap(requireNotNull(state.imageRes), command.dir)
	  go(howToStep1(img, designation))
	  state
	}


	is HowToInvoiceCommand.RequestRatingHelper -> state
  }
}

suspend fun HowToFlow.howToStep1Execute(
  command: HowToInvoiceCommand,
  state: HowToStep1State,
): HowToStep1State {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  back()
	  state
	}

	is HowToInvoiceCommand.Next -> {
	  val img = getImageBitmap(requireNotNull(state.imageRes), command.dir)
	  go(howToStep2(img, designation))
	  state
	}


	is HowToInvoiceCommand.RequestRatingHelper -> state
  }
}

fun HowToFlow.howToStep2Execute(
  command: HowToInvoiceCommand,
  state: HowToStep2State,
): HowToStep2State {
  return when (command) {
	HowToInvoiceCommand.Back -> {
	  back()
	  state
	}

	is HowToInvoiceCommand.Next -> {
	  finish(HowToFlow.Result.Finish)
	  state
	}

	is HowToInvoiceCommand.RequestRatingHelper -> state
  }
}
