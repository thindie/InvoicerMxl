package com.thindie.invoicer.application.uikit

import androidx.compose.runtime.staticCompositionLocalOf
import java.io.File


val LocalResourceDir = staticCompositionLocalOf<File> {
  error("ResourceDir is not provided")
}
