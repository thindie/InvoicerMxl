package com.thindie.invoicer.ui.main.auth

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.application.ScreenScopeError
import com.thindie.invoicer.application.ServiceCommand
import com.thindie.invoicer.application.error.AppError

fun ApplicationFlow.auth() = RouteFactory.create(
  initialState = AuthState(),
  execute = this::authExecute,
  errorMapper = { e -> this.authError(e) },
  routeContent = { AuthScreen() },
)

private fun ApplicationFlow.authError(e: Throwable) = when (e) {
  AppError.ServerError.TimeOut -> ScreenScopeError(
	message = "Проблемы с интернет соединением, запусти позже.",
	actions = buildMap {
	  put(ScreenScopeError.Actions.Common.ButtonMain, ServiceCommand.Prioritized { finish(Unit) })
	}
  )

  AppError.ServerError.RestrictedByOwner -> ScreenScopeError(
	message = "Необходимо продлить разрешение использования приложения",
	actions = buildMap {
	  put(ScreenScopeError.Actions.Common.ButtonMain, ServiceCommand.Prioritized { finish(Unit) })
	}
  )

  else -> ScreenScopeError(
	message = "Неожиданная ошибка, перезапусти позже. при повторе - свяжись  с разработчиком",
	actions = buildMap {
	  put(ScreenScopeError.Actions.Common.ButtonMain, ServiceCommand.Prioritized { finish(Unit) })
	}
  )
}