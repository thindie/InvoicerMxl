package com.thindie.invoicer.ui.main.auth

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.auth.AuthGateResult
import com.thindie.invoicer.application.auth.performAuthGate
import com.thindie.invoicer.ui.main.AppUpdateOffer
import com.thindie.invoicer.ui.main.mainRoute

suspend fun ApplicationFlow.authExecute(authCommand: AuthCommand, state: AuthState): AuthState {
  return when (authCommand) {
	AuthCommand.ConfirmRestricted -> {
	  finish(Unit)
	  state
	}

	AuthCommand.Start -> {
	  when (val gate = performAuthGate()) {
		AuthGateResult.Allowed -> {
		  go(mainRoute(null))
		  state
		}

		is AuthGateResult.AllowedWithUpdate -> {

		  go(
			mainRoute(
			  AppUpdateOffer.Soft(
				remoteVersion = gate.remoteVersionRaw,
				msiUrl = gate.msiUrl,
			  )
			)
		  )
		  state
		}
	  }
	}
  }
}