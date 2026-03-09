package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.application.RouteFactory

val InvoiceFlow.main
  get() = RouteFactory.create(
	initialState = InvoiceState(),
	execute = this::mainExecute,
	routeContent = { InvoiceScreen() }
  )
