package com.thindie.invoicer.application

import androidx.compose.runtime.Immutable

@Immutable
data class ScreenScopeError(
  val message: String,
  val actions: Map<Actions, Command>
) {
  enum class Actions {
	ButtonMain,
	ButtonSecondary,
	DismissMain,
  }
}

