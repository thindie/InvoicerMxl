package com.thindie.invoicer.ui.invoice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Stable
import com.thindie.invoicer.application.State
import com.thindie.invoicer.ui.main.Option
import java.util.*

@Stable
data class InvoiceState(
  val options: List<Option> = options(),
) : State

private fun options() = buildList {
  add(
	Option.Invoice.Simple(
	  id = UUID.randomUUID().toString(),
	  title = "Сделать простой заказ по своим продажам",
	  subtitle = "пробуем восполнить ассортимент",
	  image = Icons.Default.ShoppingCart
	)
  )
  add(
	Option.Invoice.MergeBranches(
	  id = UUID.randomUUID().toString(),
	  title = "Прокачай продажи!",
	  subtitle = "Здесь сможем сливать воедино несколько рейтингов продаж из разных филиалов",
	  image = Icons.Default.Refresh
	)
  )
}
