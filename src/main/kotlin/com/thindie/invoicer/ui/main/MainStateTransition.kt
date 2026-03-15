package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow

suspend fun ApplicationFlow.mainExecute(mainCommand: MainCommand, mainState: MainState): MainState {
  return when (mainCommand) {
	MainCommand.Exit -> {
	  finish(Unit)
	  mainState
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