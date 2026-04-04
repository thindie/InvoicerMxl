package com.thindie.invoicer.application.update

import com.thindie.invoicer.application.auth.newAuthenticatedWebdavClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
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
		throw IOException("HTTP ${response.status.value} for $msiUrl")
	  }
	  val bytes = response.body<ByteArray>()
	  Files.write(destination.toPath(), bytes)
	}
  }
}
