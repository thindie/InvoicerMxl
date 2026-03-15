package com.thindie.invoicer.ui.invoice.merge.processinvoice

import com.thindie.invoicer.application.Route
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.mergeBranchesProcessInvoice(
  source: File,
  branch: File,
  destination: File,
  limit: Int,
  offset: Int,
): Route {
  val route = RouteFactory.create(
	initialState = MergeBranchesProcessInvoiceState(
	  source = source,
	  branch = branch,
	  destination = destination,
	  limit = limit,
	  offset = offset,
	),
	execute = this::mergeInvoiceProcessExecute,
	routeContent = { MergeBranchesProcessInvoiceScreen() },
	errorMapper = { e -> invoiceFlowErrors(e) },
	initialCommand = RouteFactory.InitialCommand {
	  MergeBranchesProcessInvoiceCommand.Process(
		source = source,
		branch = branch,
		target = destination,
		limit = limit,
		offset = offset,
	  ) as MergeBranchesProcessInvoiceCommand
	},
  )
  return route
}


