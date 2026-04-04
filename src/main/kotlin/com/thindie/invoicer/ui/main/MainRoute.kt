package com.thindie.invoicer.ui.main

import com.thindie.invoicer.ApplicationFlow
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.application.ScreenScopeError
import com.thindie.invoicer.application.error.AppError

fun ApplicationFlow.mainRoute(offer: AppUpdateOffer?) = RouteFactory.create(
  initialState = MainState(updateOffer = offer),
  execute = this::mainExecute,
  routeContent = { MainScreen() },
  errorMapper = { e -> e.mainScreenScopeError }
)

private val Throwable.mainScreenScopeError get() = when (this) {
  is AppError.ServerError.UpdateVersion -> {
    ScreenScopeError(
      message = "Не удалось скачать новую версию, возможна ошибка сети или доступа",
      actions = mapOf(
        ScreenScopeError.Actions.Common.ButtonMain to MainCommand.DismissAppUpdate
      )
    )
  }
  is AppError.ServerError.ResponseMalformed -> {
    ScreenScopeError(
      message = "Полученный ответ испорчен, построение обновления невозможно",
      actions = mapOf(
        ScreenScopeError.Actions.Common.ButtonMain to MainCommand.DismissAppUpdate
      )
    )
  }

  is AppError.FileWriteError -> {
    ScreenScopeError(
      message = "Запись файла на диск вызвало ошибку, проверь диск",
      actions = mapOf(
        ScreenScopeError.Actions.Common.ButtonMain to MainCommand.DismissAppUpdate
      )
    )
  }

  else -> {
    ScreenScopeError(
      message = "Что-то пошло не так, обратись к разработчику",
      actions = mapOf(
        ScreenScopeError.Actions.Common.ButtonMain to MainCommand.DismissAppUpdate
      )
    )
  }
}
