package com.thindie.invoicer.ui.invoice

import com.thindie.invoicer.application.Router
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import com.thindie.invoicer.invoice.repository.InvoiceRepository
import com.thindie.invoicer.invoice.repository.InvoiceSummary
import com.thindie.invoicer.ui.main.Option
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

class InvoiceStateTransitionTest {

  private val repository = object : InvoiceRepository {
	override suspend fun readFileLines(path: String) = emptyList<String>()
	override suspend fun writeSimpleChildInvoice(inputPath: String, outputPath: String) =
	  InvoiceSummary("" to 0)

	override suspend fun writeMergeBranchInvoice(
	  inputPathParent: String,
	  inputPathChild: String,
	  outputPath: String,
	  limit: Int?,
	  offset: Int,
	) = InvoiceSummary("" to 0)
  }

  @Test
  fun `mainExecute Back calls router pop and returns same state`() {
	val router = mockk<Router>(relaxed = true)
	val flow = InvoiceFlow(router, repository)
	val state = invoiceStateWithStableIds()

	val out = flow.mainExecute(InvoiceCommand.Back, state)

	assertSame(state, out)
	verify(exactly = 1) { router.pop() }
  }

  @Test
  fun `mainExecute Select simple navigates to simple child`() {
	val router = mockk<Router>(relaxed = true)
	val flow = InvoiceFlow(router, repository)
	val state = invoiceStateWithStableIds()

	flow.mainExecute(InvoiceCommand.Select(state.simpleId), state)

	verify(exactly = 1) { router.push(any()) }
  }

  @Test
  fun `mainExecute Select merge navigates to merge branches`() {
	val router = mockk<Router>(relaxed = true)
	val flow = InvoiceFlow(router, repository)
	val state = invoiceStateWithStableIds()

	flow.mainExecute(InvoiceCommand.Select(state.mergeId), state)

	verify(exactly = 1) { router.push(any()) }
  }

  @Test
  fun `mainExecute Select unknown id does not navigate`() {
	val router = mockk<Router>(relaxed = true)
	val flow = InvoiceFlow(router, repository)
	val state = invoiceStateWithStableIds()

	val out = flow.mainExecute(InvoiceCommand.Select(UUID.randomUUID().toString()), state)

	assertSame(state, out)
	verify(exactly = 0) { router.push(any()) }
  }

  private fun invoiceStateWithStableIds(): InvoiceState {
	val simpleId = "opt-simple"
	val mergeId = "opt-merge"
	return InvoiceState(
	  options = listOf(
		Option.Invoice.Simple(
		  id = simpleId,
		  title = "Simple",
		  subtitle = null,
		  image = Icons.Default.ShoppingCart,
		),
		Option.Invoice.MergeBranches(
		  id = mergeId,
		  title = "Merge",
		  subtitle = null,
		  image = Icons.Default.Refresh,
		),
	  ),
	)
  }

  private val InvoiceState.simpleId: String
	get() = (options.first { it is Option.Invoice.Simple } as Option.Invoice.Simple).id

  private val InvoiceState.mergeId: String
	get() = (options.first { it is Option.Invoice.MergeBranches } as Option.Invoice.MergeBranches).id
}
