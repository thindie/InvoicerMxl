package com.thindie.invoicer.ui.main.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.uikit.AppScreen


@Composable
fun ScreenScope<AuthState, AuthCommand>.AuthScreen(
) {
  LaunchedEffect(Unit) { send(AuthCommand.Start) }
  AppScreen { }
}
