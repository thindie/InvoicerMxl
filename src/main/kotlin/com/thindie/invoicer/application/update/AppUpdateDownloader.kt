package com.thindie.invoicer.application.update

import com.thindie.invoicer.application.auth.newAuthenticatedWebdavClient
import com.thindie.invoicer.application.error.AppError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.file.Files

suspend fun downloadInstallerMsi(msiUrl: String, destination: File) {
  val client = newAuthenticatedWebdavClient()
  client.use { client ->
	withContext(Dispatchers.IO) {
	  val response: HttpResponse = client.get(msiUrl)
	  if (response.status.value !in 200..299) {
		throw AppError.ServerError.UpdateVersion
	  }
	  val bytes = try {
		response.body<ByteArray>()
	  } catch (e: NoTransformationFoundException) {
		throw AppError.ServerError.ResponseMalformed(e.cause, e.message, payload = response)
	  } catch (e: DoubleReceiveException) {
		throw AppError.ServerError.ResponseMalformed(e.cause, e.message, payload = response)
	  }

	  try {
		Files.write(destination.toPath(), bytes)
	  } catch (e: IOException) {
		throw AppError.FileWriteError(e.cause, e.message)
	  } catch (e: Throwable) {
		throw AppError.UnexpectedError(e.cause, e.message)
	  }
	}
  }
}
