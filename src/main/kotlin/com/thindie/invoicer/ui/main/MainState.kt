package com.thindie.invoicer.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.thindie.invoicer.application.State
import java.util.*

@Immutable
data class MainState(
  val greeting: String = "TODO greeting",
  val options: List<Option> = options(),
  val visibleHint: String? = null,
) : State

@Immutable
sealed interface Option {
  val id: String
  val title: String
  val subtitle: String?
  val image: ImageVector?

  data class Invoice(
	override val id: String,
	override val title: String,
	override val subtitle: String?,
	override val image: ImageVector? = null,
  ) : Option

  data class Stub(
	override val id: String,
	override val title: String,
	override val subtitle: String?,
	override val image: ImageVector? = null,
  ) : Option
}

private fun options() = buildList {
  add(
	Option.Invoice(
	  id = UUID.randomUUID().toString(),
	  title = "Сделать заказ",
	  subtitle = null,
	  image = Icons.Default.ShoppingCart
	)
  )
  add(
	Option.Stub(
	  id = UUID.randomUUID().toString(),
	  title = "В разработке",
	  subtitle = "Ожидаем полезный функционал позже",
	  image = Icons.Default.Build
	)
  )
}