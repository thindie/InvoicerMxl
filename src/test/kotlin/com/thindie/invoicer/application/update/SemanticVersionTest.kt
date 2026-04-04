package com.thindie.invoicer.application.update

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SemanticVersionTest {

  @Test
  fun parseTrimsAndStripsVPrefix() {
	assertEquals(SemanticVersion(2, 1, 3), SemanticVersion.parse("  v2.1.3  "))
	assertEquals(SemanticVersion(2, 1, 3), SemanticVersion.parse("V2.1.3"))
  }

  @Test
  fun parsePadsMissingSegments() {
	assertEquals(SemanticVersion(3, 0, 0), SemanticVersion.parse("3"))
	assertEquals(SemanticVersion(1, 2, 0), SemanticVersion.parse("1.2"))
  }

  @Test
  fun parseFourOrMoreSegmentsUsesFirstThree() {
	assertEquals(SemanticVersion(1, 2, 3), SemanticVersion.parse("1.2.3.4"))
	assertEquals(SemanticVersion(10, 20, 30), SemanticVersion.parse("10.20.30.99"))
  }

  @Test
  fun parseAcceptsLeadingZerosPerSegment() {
	assertEquals(SemanticVersion(2, 5, 0), SemanticVersion.parse("02.05.00"))
  }

  @Test
  fun parseRejectsNonNumeric() {
	assertNull(SemanticVersion.parse("2.x.0"))
	assertNull(SemanticVersion.parse(""))
	assertNull(SemanticVersion.parse("   "))
	assertNull(SemanticVersion.parse("a.b.c"))
	assertNull(SemanticVersion.parse("2.0.0-beta"))
  }

  @Test
  fun parseRejectsEmbeddedNewlineInSegment() {
	assertNull(SemanticVersion.parse("2.0.0\n3.0.0"))
	assertNull(SemanticVersion.parse("2\n.0.0"))
  }

  @Test
  fun parseTrimsTrailingNewlineFromWholeString() {
	assertEquals(SemanticVersion(1, 0, 0), SemanticVersion.parse("1.0.0\n"))
	assertEquals(SemanticVersion(2, 3, 4), SemanticVersion.parse("\r\n2.3.4\r\n"))
  }

  @Test
  fun compareVersions() {
	assertTrue(SemanticVersion.parse("2.0.1")!! > SemanticVersion.parse("2.0.0")!!)
	assertTrue(SemanticVersion.parse("2.1.0")!! > SemanticVersion.parse("2.0.9")!!)
	assertTrue(SemanticVersion.parse("3.0.0")!! > SemanticVersion.parse("2.9.9")!!)
  }

  @Test
  fun compareEqualAndReflexive() {
	val a = SemanticVersion(2, 0, 0)
	val b = SemanticVersion(2, 0, 0)
	assertEquals(0, a.compareTo(b))
	assertTrue(a == b)
  }

  @Test
  fun dataClassEqualityByComponents() {
	val v = SemanticVersion(1, 0, 0)
	assertEquals(v, v)
	assertNotEquals(SemanticVersion(1, 0, 0), SemanticVersion(1, 0, 1))
	assertTrue(v < SemanticVersion(1, 0, 1))
  }
}
