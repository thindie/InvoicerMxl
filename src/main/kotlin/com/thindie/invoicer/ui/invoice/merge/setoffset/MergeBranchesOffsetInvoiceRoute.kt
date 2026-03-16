package com.thindie.invoicer.ui.invoice.merge.setoffset

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.mergeBranchesOffsetInvoice(
  source: File,
  branch: File,
  destination: File,
  limit: Int?
) = RouteFactory.create(
  initialState = MergeBranchesOffsetInvoiceState(
    source = source,
    branch = branch,
    destination = destination,
    limit = limit
  ),
  execute = this::mergeBranchesOffsetInvoiceExecute,
  routeContent = { MergeBranchesOffsetInvoiceScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)
