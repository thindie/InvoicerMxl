package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.application.ScreenScopeError
import com.thindie.invoicer.application.ServiceCommand
import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.simpleChildProcessInvoice() = RouteFactory.create(
  initialState = SimpleChildProcessInvoiceState,
  execute = this::simpleInvoiceExecute,
  routeContent = { SimpleChildProcessInvoiceScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)


