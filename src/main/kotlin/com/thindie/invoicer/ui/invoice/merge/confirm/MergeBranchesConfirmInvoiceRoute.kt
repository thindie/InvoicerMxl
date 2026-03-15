package com.thindie.invoicer.ui.invoice.merge.confirm

import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import java.io.File

fun InvoiceFlow.mergeBranchesConfirmInvoice(source: File, branch: File) = RouteFactory.create(
  initialState = MergeBranchesConfirmInvoiceState(source = source, branch = branch),
  execute = this::simpleInvoiceExecute,
  routeContent = { MergeBranchesConfirmInvoiceScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)
