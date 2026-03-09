package com.thindie.invoicer.ui.main

import com.thindie.invoicer.application.Command

sealed interface MainCommand : Command {
  data object Exit : MainCommand
  data class ToggleOption(val id: String) : MainCommand
  data object Confirm : MainCommand
}
