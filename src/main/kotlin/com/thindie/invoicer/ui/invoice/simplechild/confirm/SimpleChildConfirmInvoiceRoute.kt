package com.thindie.invoicer.ui.invoice.simplechild.confirm

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.simpleChildConfirmInvoice(source: File) = RouteFactory.create(
  initialState = SimpleChildConfirmInvoiceState(source = source),
  execute = this::simpleInvoiceExecute,
  routeContent = { SimpleChildConfirmInvoiceScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)
