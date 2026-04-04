package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openSaveFileChooser
import com.thindie.invoicer.application.update.downloadInstallerMsi
import java.io.IOException

suspend fun ApplicationFlow.mainExecute(mainCommand: MainCommand, mainState: MainState): MainState {
  return when (mainCommand) {
	MainCommand.Exit -> {
	  finish(Unit)
	  mainState
	}

	MainCommand.DismissAppUpdate -> mainState.copy(updateOffer = null)

	is MainCommand.SaveAppInstaller -> {
	  val offer = mainState.updateOffer ?: return mainState
	  val destination = openSaveFileChooser(
		fileChooser = mainCommand.chooser,
		suggestedFileName = mainCommand.suggestedFileName,
		title = "Сохранить MSI установщика",
	  ) ?: return mainState
	  try {
		downloadInstallerMsi(offer.msiUrl, destination)
	  } catch (e: IOException) {
		throw AppError.FileWriteError(e.cause, e.message)
	  }
	  mainState.copy(updateOffer = null)
	}

	is MainCommand.Select -> {
	  val toggled = mainState.options.find { it.id == mainCommand.id }
	  when (toggled) {
		is Option.Invoice -> {
		  startInvoiceFlow()
		  mainState
		}

		else -> {
		  mainState
		}
	  }
	}
  }
}