package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow

val InvoiceFlow.simpleChildInvoice
  get() = RouteFactory.create(
	initialState = SimpleChildInvoiceState(),
	execute = this::simpleInvoiceExecute,
	routeContent = { SimpleChildInvoiceScreen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )
