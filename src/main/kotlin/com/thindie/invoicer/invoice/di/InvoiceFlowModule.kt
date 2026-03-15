package com.thindie.invoicer.invoice.di

import com.thindie.invoicer.invoice.repository.InvoiceRepository
import com.thindie.invoicer.invoice.repository.InvoiceRepositoryImpl
import java.io.File

object InvoiceFlowModule {
  private const val BINARY_NAME = "st.dll"
  private val resDir = System.getProperty("compose.application.resources_dir")
	?.let { File(it) }
	?: File("src/appData/common")

  val invoiceRepository: InvoiceRepository = InvoiceRepositoryImpl(
	filename = BINARY_NAME,
	filePath = resDir
  )
}
