package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.*
import com.thindie.invoicer.application.error.AppError

class HowToFlow(private val router: Router) : ScreenFlow<Route, HowToFlow.Result>(router) {
  override fun start() {
	router.push(this.howToIntro)
  }

  fun invoiceFlowErrors(e: Throwable): ScreenScopeError =
	when (e) {
	  is AppError -> when (e) {
		is AppError.FilePathError,
		is AppError.FileReadError,
		is AppError.FileWriteError -> ScreenScopeError(
		  message = "Что-то произошло в процессе работы с чтением/записи файла, возможно нет прав записи или не хватает места",
		  actions = buildMap {
			put(
			  ScreenScopeError.Actions.Common.ButtonMain,
			  ServiceCommand.Prioritized { finish(Result.Error) }
			)
		  }
		)

		is AppError.MalformedSaleRatingError -> ScreenScopeError(
		  message = "Неверный формат данных рейтинга продаж",
		  actions = buildMap {
			put(
			  ScreenScopeError.Actions.Common.ButtonMain,
			  ServiceCommand.Prioritized { back() }
			)
		  }
		)

		is AppError.Parse1CBinaryError -> ScreenScopeError(
		  message = "Не удалось разобрать бинарный файл 1С, обратитесь к разработчику софта",
		  actions = buildMap {
			put(
			  ScreenScopeError.Actions.Common.ButtonMain,
			  ServiceCommand.Prioritized { finish(Result.Error) }
			)
		  }
		)

		is AppError.UnexpectedError -> invoiceFlowErrorsInternal
	  }

	  else -> invoiceFlowErrorsInternal
	}

  private val invoiceFlowErrorsInternal = ScreenScopeError(
	message = "Неожиданная ошибка",
	actions = buildMap {
	  put(ScreenScopeError.Actions.Common.ButtonMain, ServiceCommand.Prioritized { finish(Result.Error) })
	}
  )

  enum class Result {
	Finish,
	Error,
  }
}
