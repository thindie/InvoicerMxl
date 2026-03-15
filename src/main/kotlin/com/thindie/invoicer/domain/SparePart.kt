package com.thindie.invoicer.domain

import androidx.compose.runtime.Immutable
import com.thindie.invoicer.invoice.repository.InvoiceParser

@Immutable
data class SparePart(
  val vendorCode: String,
  val sales: Int,
  val stock: Int,
  val rank: Int
) {
  override fun equals(other: Any?): Boolean {
	if (other is SparePart) {
	  return this.vendorCode == (other).vendorCode
	}
	return false
  }

  override fun toString(): String {
	return String.format("%d   %s : sales %d  stocks %d //", rank, vendorCode, sales, stock)
  }

  override fun hashCode(): Int {
	return vendorCode.hashCode()
  }
}

val emptySparePart get() = SparePart(
  vendorCode = InvoiceParser.Anchor.MAX_VALUE,
  sales = 0,
  stock = 0,
  rank = 0
)

