package com.thindie.invoicer.application.update

data class SemanticVersion(
  val major: Int,
  val minor: Int,
  val patch: Int,
) : Comparable<SemanticVersion> {

  override fun compareTo(other: SemanticVersion): Int {
	val cMajor = major.compareTo(other.major)
	if (cMajor != 0) return cMajor
	val cMinor = minor.compareTo(other.minor)
	if (cMinor != 0) return cMinor
	return patch.compareTo(other.patch)
  }

  companion object {
	fun parse(raw: String): SemanticVersion? {
	  val trimmed = raw.trim().lowercase().removePrefix("v").trim()
	  if (trimmed.isEmpty()) return null
	  val segments = trimmed.split('.')
	  if (segments.isEmpty()) return null
	  val numbers = segments.mapNotNull { segment -> segment.toIntOrNull() }
	  if (numbers.size != segments.size) return null
	  if (numbers.isEmpty()) return null
	  val major = numbers[0]
	  val minor = numbers.getOrElse(1) { 0 }
	  val patch = numbers.getOrElse(2) { 0 }
	  return SemanticVersion(major, minor, patch)
	}
  }
}
