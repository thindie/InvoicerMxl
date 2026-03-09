package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow

suspend fun ApplicationFlow.mainExecute(mainCommand: MainCommand, mainState: MainState): MainState {
  return when (mainCommand) {
	MainCommand.Exit -> {
	  finish(Unit)
	  mainState
	}

	MainCommand.Confirm -> {
	  when (mainState.selected) {
		is Option.Invoice -> {
		  startInvoiceFlow()
		  mainState
		}

		null -> {
		  mainState
		}
	  }
	}

	is MainCommand.ToggleOption -> {
	  val toggled = mainCommand.id
	  if (mainState.selected?.id == toggled) {
		mainState.copy(selected = null)
	  } else mainState.copy(selected = mainState.options.find { it.id == toggled })
	}
  }
}