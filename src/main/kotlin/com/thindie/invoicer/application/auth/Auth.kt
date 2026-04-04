package com.thindie.invoicer.application.auth

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.update.AppVersion
import com.thindie.invoicer.application.update.SemanticVersion
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException

private const val USERNAME = "111"
private const val PWD = "1111"

/** Version file on WebDAV: body is a semver line (e.g. `2.0.1`), or legacy `ok`. */
internal const val URL = "URL"

internal fun msiUrlAdjacentToVersionFile(versionFileUrl: String): String {
  val slash = versionFileUrl.lastIndexOf('/')
  val base = if (slash >= 0) versionFileUrl.substring(0, slash + 1) else "$versionFileUrl/"
  return "${base}InvoicerMxl.msi"
}

internal fun newAuthenticatedWebdavClient(): HttpClient =
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

sealed class AuthGateResult {
  data object Allowed : AuthGateResult()
  data class AllowedWithUpdate(
	val remoteVersionRaw: String,
	val msiUrl: String,
  ) : AuthGateResult()
}

/**
 * Pure parsing / comparison of the version file body (already trimmed). For unit tests.
 */
internal fun authGateResultForVersionResponse(
  responseBodyTrimmed: String,
  localSemver: String,
  versionCheckUrl: String,
): AuthGateResult {
  if (responseBodyTrimmed.isEmpty()) {
	throw AppError.ServerError.RestrictedByOwner
  }
  if (responseBodyTrimmed.equals("ok", ignoreCase = true)) {
	return AuthGateResult.Allowed
  }
  val remote = SemanticVersion.parse(responseBodyTrimmed)
	?: throw AppError.ServerError.RestrictedByOwner
  val local = SemanticVersion.parse(localSemver)
	?: return AuthGateResult.Allowed
  return if (remote > local) {
	AuthGateResult.AllowedWithUpdate(
	  remoteVersionRaw = responseBodyTrimmed,
	  msiUrl = msiUrlAdjacentToVersionFile(versionCheckUrl),
	)
  } else {
	AuthGateResult.Allowed
  }
}

suspend fun performAuthGate(): AuthGateResult {
  val client = newAuthenticatedWebdavClient()
  return client.use { client ->
	val response = withTimeoutOrNull(5_000L) {
	  client.get(URL)
	}
	if (response == null) {
	  throw AppError.ServerError.TimeOut
	}
	val body = try {
	  response.body<String>().trim()
	} catch (_: IOException) {
	  throw AppError.ServerError.TimeOut
	}
	authGateResultForVersionResponse(body, AppVersion.SEMVER, URL)
  }
}
