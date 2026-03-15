package com.thindie.invoicer.ui.invoice.merge.setlimit

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.mergeBranchesLimitInvoice(source: File, branch: File, destination: File,) = RouteFactory.create(
  initialState = MergeBranchesLimitInvoiceState(source = source, branch = branch, destination = destination),
  execute = this::mergeBranchesLimitInvoiceExecute,
  routeContent = { MergeBranchesLimitInvoiceScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)
