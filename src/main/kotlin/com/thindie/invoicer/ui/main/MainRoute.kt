package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.application.ScreenScopeError

val ApplicationFlow.main
  get() = RouteFactory.create(
	initialState = MainState(),
	execute = this::mainExecute,
	routeContent = { MainScreen() },
	errorMapper = { _ -> mainScreenScopeError }
  )

private val mainScreenScopeError = ScreenScopeError(
  message = "main error",
  actions = mapOf(
	ScreenScopeError.Actions.ButtonMain to MainCommand.Exit
  )
)