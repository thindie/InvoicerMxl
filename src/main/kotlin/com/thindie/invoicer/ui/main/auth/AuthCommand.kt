package com.thindie.invoicer.ui.main.auth

import com.thindie.invoicer.application.Command

sealed interface AuthCommand : Command {
  data object Start: AuthCommand
  data object ConfirmRestricted : AuthCommand
}
