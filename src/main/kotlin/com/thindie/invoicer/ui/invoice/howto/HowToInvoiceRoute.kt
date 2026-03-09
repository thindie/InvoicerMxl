package com.thindie.invoicer.ui.invoice.howto

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow

val InvoiceFlow.howTo
  get() = RouteFactory.create(
	initialState = HowToInvoiceState(),
	execute = this::howToExecute,
	routeContent = { HowToInvoiceScreen() }
  )
