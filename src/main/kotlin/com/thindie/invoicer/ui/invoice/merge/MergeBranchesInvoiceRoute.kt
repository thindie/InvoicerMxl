package com.thindie.invoicer.ui.invoice.merge

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow

val InvoiceFlow.mergeBranchesInvoice
  get() = RouteFactory.create(
	initialState = MergeBranchesInvoiceState(),
	execute = this::simpleInvoiceExecute,
	routeContent = { MergeBranchesInvoiceScreen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )
