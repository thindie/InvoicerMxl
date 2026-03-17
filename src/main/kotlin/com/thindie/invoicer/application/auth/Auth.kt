package com.thindie.invoicer.application.auth

import com.thindie.invoicer.application.error.AppError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withTimeoutOrNull

private const val USERNAME = "111"
private const val PWD = "1111"

private val client = MutableStateFlow<HttpClient?>(
  HttpClient(CIO) {
	install(Auth) {
	  basic {
		credentials {
		  BasicAuthCredentials(username = USERNAME, password = PWD)
		}
		sendWithoutRequest { true }
	  }
	}
  }
)

suspend fun authPassed(): Boolean {
  val response = withTimeoutOrNull(5_000L) {
	client.value?.get(URL)
  }
  when (response?.body<String>().toString()) {
	"ok" -> true
	"null" -> throw AppError.ServerError.TimeOut
	else -> throw AppError.ServerError.RestrictedByOwner
  }

  client.value?.close()
  client.value = null
  return true
}

private const val URL = "1111"