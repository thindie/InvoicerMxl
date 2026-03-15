package com.thindie.invoicer.ui.invoice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Stable
import com.thindie.invoicer.application.State
import com.thindie.invoicer.ui.main.Option
import java.util.UUID

@Stable
data class InvoiceState(
  val options: List<Option> = options(),
) : State

private fun options() = buildList {
  add(
	Option.Invoice(
	  id = UUID.randomUUID().toString(),
	  title = "Сделать простой заказ по своим продажам",
	  subtitle = "пробуем восполнить ассортимент",
	  image = Icons.Default.ShoppingCart
	)
  )
  add(
	Option.Stub(
	  id = UUID.randomUUID().toString(),
	  title = "В разработке",
	  subtitle = "Здесь сможем сливать воедино несколько рейтингов продаж из разных филиалов",
	  image = Icons.Default.Build
	)
  )
}
