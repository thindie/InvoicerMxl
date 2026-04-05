package com.thindie.invoicer.ui.main

import com.thindie.invoicer.application.Command
import javax.swing.JFileChooser

sealed interface MainCommand : Command {
  data object Exit : MainCommand
  data class Select(val id: String) : MainCommand
  data object DismissAppUpdate : MainCommand
  data class SaveAppInstaller(
	val chooser: JFileChooser,
	val suggestedFileName: String,
  ) : MainCommand
}
