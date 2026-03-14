package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.ui.graphics.ImageBitmap
import com.thindie.invoicer.application.RouteFactory
import com.thindie.invoicer.ui.invoice.InvoiceFlow

val HowToFlow.howToIntro
  get() = RouteFactory.create(
	initialState = HowToIntroState(),
	execute = this::howToIntroExecute,
	routeContent = { HowToIntroScreen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )

fun HowToFlow.howToStep0(stepIllustration: ImageBitmap?) = RouteFactory.create(
	initialState = HowToStep0State(image = stepIllustration),
	execute = this::howToStep0Execute,
	routeContent = { HowToStep0Screen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )

fun HowToFlow.howToStep1(stepIllustration: ImageBitmap?) = RouteFactory.create(
	initialState = HowToStep1State(image = stepIllustration),
	execute = this::howToStep1Execute,
	routeContent = { HowToStep1Screen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )

fun HowToFlow.howToStep2(stepIllustration: ImageBitmap?) = RouteFactory.create(
	initialState = HowToStep2State(image = stepIllustration),
	execute = this::howToStep2Execute,
	routeContent = { HowToStep2Screen() },
	errorMapper = { e -> invoiceFlowErrors(e) }
  )

val HowToFlow.howTo
  get() = howToIntro

