package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.application.Route
import com.thindie.invoicer.application.Router
import com.thindie.invoicer.application.ScreenFlow

class InvoiceFlow(private val router: Router): ScreenFlow<Route, Unit>(router) {
  override fun start() {
	router.push(this.main)
  }
}
