package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.*
import com.thindie.invoicer.application.error.AppError

class HowToFlow(private val router: Router) : ScreenFlow<Route, HowToFlow.Result>(router) {

  private var designationNullable: Designation? = null
  val designation: Designation get() = requireNotNull(designationNullable)

  enum class Designation {
	UpdateStock,
	MergeUpdateStock
  }

  fun withParams(type: Designation): HowToFlow {
	designationNullable = type
	return this
  }

  override fun start() {
	router.push(this.howToIntro(designation))
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

		is AppError.WrongPreconditionsRequested -> ScreenScopeError(
		  message = "Похоже что-то с лимитами или оффсетами. А может с ними обоими. Попробуй снять их.",
		  actions = buildMap {
			put(
			  ScreenScopeError.Actions.Common.ButtonMain,
			  ServiceCommand.Prioritized { finish(Result.Error) }
			)
		  }
		)

		is AppError.UnexpectedError -> invoiceFlowErrorsInternal
		else -> invoiceFlowErrorsInternal
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
