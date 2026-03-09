package com.thindie.invoicer.application.error

sealed class AppError: Exception() {
  data class FileReadError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()

  data class FileWriteError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()

  data class FilePathError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()

  data class UnexpectedError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()

  data class Parse1CBinaryError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()

  data class MalformedSaleRatingError(
	override val cause: Throwable?,
	override val message: String?,
  ): AppError()
}
