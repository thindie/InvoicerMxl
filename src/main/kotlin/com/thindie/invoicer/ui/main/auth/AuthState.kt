package com.thindie.invoicer.ui.main.auth

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.application.State

@Immutable
data class AuthState(val showRestrictDialog: Boolean = false) : State
