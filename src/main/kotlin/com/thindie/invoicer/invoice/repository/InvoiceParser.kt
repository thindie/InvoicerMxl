package com.thindie.invoicer.invoice.repository

object InvoiceParser {
  const val PARSE_SCHEMA_SIZE = 99
  const val PARSE_SCHEMA_SUFFIX = ".mxl"
  const val RESULT_PREFIX = "_inv_"
  const val RESULT_TIMESTAMP_LENGTH = 7
  const val CHARSET_WINDOWS_1251 = "Windows-1251"
  const val ITERATIONS = 1

  object Anchor {
	const val FIRST_CHAR = "Ц"
	const val SECOND_CHAR = "Б"
	const val MAX_VALUE = "ЦБ999999"
	const val LENGTH = 8
  }
}
