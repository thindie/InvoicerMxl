package com.thindie.invoicer.invoice.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Simple-child order must not include positions whose stock column in the sales rating file is greater than zero.
 */
class SaleRatingSimpleChildParsingTest {

  @Test
  fun `saleRatingLinesForSimpleChildOrder drops lines with positive stock`() {
	val lineRestock = "1\tЦБ11111111\t100\t0"
	val lineHasStock = "2\tЦБ22222222\t50\t7"
	val lineAlsoRestock = "3\tЦБ33333333\t0\t0"

	val parts = InvoiceRepositoryImpl.saleRatingLinesForSimpleChildOrder(
	  listOf(lineRestock, lineHasStock, lineAlsoRestock),
	)

	assertEquals(
	  setOf("ЦБ111111", "ЦБ333333"),
	  parts.map { it.vendorCode }.toSet(),
	)
	assertTrue(parts.none { it.stock > 0 })
  }

  @Test
  fun `saleRatingLinesForSimpleChildOrder is empty when every line has positive stock`() {
	val lines = listOf(
	  "1\tЦБ11111111\t10\t1",
	  "2\tЦБ22222222\t20\t100",
	)
	val parts = InvoiceRepositoryImpl.saleRatingLinesForSimpleChildOrder(lines)
	assertTrue(parts.isEmpty())
  }

  @Test
  fun `parseRatingLine reads stock from last column`() {
	val line = "5\tЦБ999999\t3\t0"
	val part = InvoiceRepositoryImpl.parseRatingLine(line)!!
	assertEquals(5, part.rank)
	assertEquals(3, part.sales)
	assertEquals(0, part.stock)
	assertEquals("ЦБ999999", part.vendorCode)
  }
}
