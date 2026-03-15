package com.thindie.invoicer.ui.invoice.simplechild.processinvoice

import com.thindie.invoicer.application.Route
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.simpleChildProcessInvoice(source: File, destination: File): Route {
  val route = RouteFactory.create(
	initialState = SimpleChildProcessInvoiceState(source, destination),
	execute = this::simpleInvoiceExecute,
	routeContent = { SimpleChildProcessInvoiceScreen() },
	errorMapper = { e -> invoiceFlowErrors(e) },
	initialCommand = RouteFactory.InitialCommand {
	  SimpleChildProcessInvoiceCommand.Process(
		source = source,
		destination
	  ) as SimpleChildProcessInvoiceCommand
	},
  )
  return route
}


