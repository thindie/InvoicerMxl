package com.thindie.invoicer.ui.main.auth

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.auth.authPassed
import com.thindie.invoicer.ui.main.main

suspend fun ApplicationFlow.authExecute(authCommand: AuthCommand, state: AuthState): AuthState {
  return when (authCommand) {
	AuthCommand.ConfirmRestricted -> {
	  finish(Unit)
	  state
	}
	AuthCommand.Start -> {
	  val passed = authPassed()
	  if (passed) {
		go(this.main)
		state
	  } else {
		state.copy(showRestrictDialog = true)
	  }
	}
  }
}