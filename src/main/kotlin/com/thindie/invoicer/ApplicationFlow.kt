package com.thindie.invoicer

import com.thindie.invoicer.application.Route
import com.thindie.invoicer.application.Router
import com.thindie.invoicer.application.ScreenFlow
import com.thindie.invoicer.invoice.di.InvoiceFlowModule
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import com.thindie.invoicer.ui.main.main

class ApplicationFlow(private val router: Router) : ScreenFlow<Route, Unit>(router) {
  override fun start() {
	val main = this.main
	router.push(main)
  }

  fun startInvoiceFlow() {
	InvoiceFlow(router, repository = InvoiceFlowModule.invoiceRepository)
	  .onFinishBuilder { result ->
		when (result) {
		  InvoiceFlow.Result.Success -> Unit
		  InvoiceFlow.Result.Finish -> finish(Unit)
		  InvoiceFlow.Result.Error -> Unit
		}
	  }
	  .start()
  }
}