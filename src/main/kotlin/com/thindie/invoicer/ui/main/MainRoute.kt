package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.application.ScreenScopeError

fun ApplicationFlow.mainRoute(offer: AppUpdateOffer?) = RouteFactory.create(
  initialState = MainState(updateOffer = offer),
  execute = this::mainExecute,
  routeContent = { MainScreen() },
  errorMapper = { _ -> mainScreenScopeError }
)

private val mainScreenScopeError = ScreenScopeError(
  message = "Критическая ошибка, работа прервана",
  actions = mapOf(
	ScreenScopeError.Actions.Common.ButtonMain to MainCommand.Exit
  )
)