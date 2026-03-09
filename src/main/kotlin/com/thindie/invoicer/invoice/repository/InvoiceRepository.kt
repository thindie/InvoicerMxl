package com.thindie.invoicer.invoice.repository

interface InvoiceRepository {
  suspend fun readFileLines(path: String): List<String>
  suspend fun writeSimpleChildInvoice(inputPath: String, outputPath: String)
}
