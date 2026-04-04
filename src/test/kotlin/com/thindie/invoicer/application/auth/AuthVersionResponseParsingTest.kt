package com.thindie.invoicer.application.auth

import com.thindie.invoicer.application.error.AppError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class AuthVersionResponseParsingTest {

  private val versionFileUrl = "https://example.org/webdav/index.txt.txt"

  @Test
  fun legacyOkAllowsRegardlessOfLocalVersion() {
	assertEquals(
	  AuthGateResult.Allowed,
	  authGateResultForVersionResponse("OK", "99.0.0", versionFileUrl),
	)
  }

  @Test
  fun emptyBodyThrowsRestricted() {
	assertFailsWith<AppError.ServerError.RestrictedByOwner> {
	  authGateResultForVersionResponse("", "2.0.0", versionFileUrl)
	}
  }

  @Test
  fun nonSemverBodyThrowsRestricted() {
	assertFailsWith<AppError.ServerError.RestrictedByOwner> {
	  authGateResultForVersionResponse("2.x.0", "2.0.0", versionFileUrl)
	}
  }

  @Test
  fun remoteNewerReturnsUpdateWithMsiNextToVersionFile() {
	val result = authGateResultForVersionResponse("2.1.0", "2.0.0", versionFileUrl)
	val update = result as AuthGateResult.AllowedWithUpdate
	assertEquals("2.1.0", update.remoteVersionRaw)
	assertEquals("https://example.org/webdav/InvoicerMxl.msi", update.msiUrl)
  }

  @Test
  fun remoteEqualAllows() {
	assertEquals(
	  AuthGateResult.Allowed,
	  authGateResultForVersionResponse("2.0.0", "2.0.0", versionFileUrl),
	)
  }

  @Test
  fun remoteOlderAllows() {
	assertEquals(
	  AuthGateResult.Allowed,
	  authGateResultForVersionResponse("1.0.0", "2.0.0", versionFileUrl),
	)
  }

  @Test
  fun unparsableLocalSemverAllowsWithoutTreatingAsUpdate() {
	assertEquals(
	  AuthGateResult.Allowed,
	  authGateResultForVersionResponse("3.0.0", "not-a-version", versionFileUrl),
	)
  }

  @Test
  fun msiUrlAdjacentReplacesPathAfterLastSlash() {
	assertEquals(
	  "https://host/a/b/InvoicerMxl.msi",
	  msiUrlAdjacentToVersionFile("https://host/a/b/index.txt.txt"),
	)
  }

  @Test
  fun msiUrlAdjacentWhenNoSlashAppendsUnderSyntheticFolder() {
	assertEquals(
	  "x/InvoicerMxl.msi",
	  msiUrlAdjacentToVersionFile("x"),
	)
  }
}
