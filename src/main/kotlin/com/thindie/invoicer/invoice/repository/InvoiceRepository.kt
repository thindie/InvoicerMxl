package com.thindie.invoicer.invoice.repository

interface InvoiceRepository {
  suspend fun readFileLines(path: String): List<String>
  suspend fun writeSimpleChildInvoice(inputPath: String, outputPath: String): InvoiceSummary
  suspend fun writeMergeBranchInvoice(
    inputPathParent: String,
    inputPathChild: String,
    outputPath: String,
    limit: Int?,
    offset: Int
  ): InvoiceSummary
}

@JvmInline
value class InvoiceSummary(val value: Pair<String, Int>) {
  val outputPath: String get() = value.first
  val filesProduced: Int get() = value.second
}
