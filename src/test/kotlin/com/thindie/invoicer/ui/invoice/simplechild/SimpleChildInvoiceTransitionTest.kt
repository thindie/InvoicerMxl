package com.thindie.invoicer.ui.invoice.simplechild

import com.thindie.invoicer.application.Router
import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.application.uikit.openFileChooser
import com.thindie.invoicer.invoice.repository.InvoiceRepository
import com.thindie.invoicer.invoice.repository.InvoiceSummary
import com.thindie.invoicer.ui.invoice.InvoiceFlow
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.io.File
import javax.swing.JFileChooser

class SimpleChildInvoiceTransitionTest {

  @AfterEach
  fun tearDown() {
	unmockkAll()
  }

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

  private fun flow(router: Router) = InvoiceFlow(router, repository)

  @Test
  fun `simpleInvoiceExecute Back pops router`() = runTest {
	val router = mockk<Router>(relaxed = true)
	flow(router).simpleInvoiceExecute(SimpleChildInvoiceCommand.Back, SimpleChildInvoiceState())
	verify(exactly = 1) { router.pop() }
  }

  @Test
  fun `simpleInvoiceExecute Finish calls pop on empty stack`() = runTest {
	val router = mockk<Router>(relaxed = true)
	flow(router).simpleInvoiceExecute(SimpleChildInvoiceCommand.Finish, SimpleChildInvoiceState())
	verify(exactly = 1) { router.pop() }
  }

  @Test
  fun `simpleInvoiceExecute HowTo pushes route`() = runTest {
	val router = mockk<Router>(relaxed = true)
	flow(router).simpleInvoiceExecute(SimpleChildInvoiceCommand.HowTo, SimpleChildInvoiceState())
	verify(atLeast = 1) { router.push(any()) }
  }

  @Test
  fun `simpleInvoiceExecute Confirm without source in state fails`() = runTest {
	val router = mockk<Router>(relaxed = true)
	val temp = File.createTempFile("src", ".txt")
	try {
	  var caught: Throwable? = null
	  try {
		flow(router).simpleInvoiceExecute(
		  SimpleChildInvoiceCommand.Confirm(temp),
		  SimpleChildInvoiceState(source = null),
		)
	  } catch (t: Throwable) {
		caught = t
	  }
	  assertTrue(caught is IllegalArgumentException, "expected requireNotNull failure, got $caught")
	} finally {
	  temp.delete()
	}
  }

  @Test
  fun `simpleInvoiceExecute Confirm with source navigates`() = runTest {
	val router = mockk<Router>(relaxed = true)
	val temp = File.createTempFile("src", ".txt")
	try {
	  val state = SimpleChildInvoiceState(source = temp)
	  flow(router).simpleInvoiceExecute(SimpleChildInvoiceCommand.Confirm(temp), state)
	  verify(exactly = 1) { router.push(any()) }
	} finally {
	  temp.delete()
	}
  }

  @Test
  fun `simpleInvoiceExecute OpenSource updates source from chooser`() = runTest {
	val picked = File.createTempFile("picked", ".dat")
	try {
	  coEvery {
		openFileChooser(any(), any(), any(), any())
	  } returns picked

	  val router = mockk<Router>(relaxed = true)
	  val flow = flow(router)
	  val chooser = JFileChooser()
	  val next = flow.simpleInvoiceExecute(
		SimpleChildInvoiceCommand.OpenSource(
		  chooser = chooser,
		  title = "Pick",
		  type = JFileChooser.OPEN_DIALOG,
		),
		SimpleChildInvoiceState(),
	  )
	  assertEquals(picked, next.source)
	} finally {
	  picked.delete()
	}
  }

  @Test
  fun `simpleInvoiceExecute OpenSource wraps chooser failure as FileReadError`() = runTest {
	coEvery {
	  openFileChooser(any(), any(), any(), any())
	} throws java.io.IOException("io failed")

	val router = mockk<Router>(relaxed = true)
	val flow = flow(router)
	var caught: Throwable? = null
	try {
	  flow.simpleInvoiceExecute(
		SimpleChildInvoiceCommand.OpenSource(
		  chooser = JFileChooser(),
		  title = "Pick",
		  type = JFileChooser.OPEN_DIALOG,
		),
		SimpleChildInvoiceState(),
	  )
	  fail("expected AppError.FileReadError")
	} catch (t: Throwable) {
	  caught = t
	}
	assertTrue(caught is AppError.FileReadError)
	val err = caught as AppError.FileReadError
	assertTrue(err.message!!.contains("io failed"))
  }

  @Test
  fun `simpleInvoiceExecute OpenSource returns same state when file is null`() = runTest {
	coEvery {
	  openFileChooser(any(), any(), any(), any())
	} returns null

	val router = mockk<Router>(relaxed = true)
	val prior = SimpleChildInvoiceState()
	val next = flow(router).simpleInvoiceExecute(
	  SimpleChildInvoiceCommand.OpenSource(
		chooser = JFileChooser(),
		title = "Pick",
		type = JFileChooser.OPEN_DIALOG,
	  ),
	  prior,
	)
	assertEquals(null, next.source)
	assertEquals(prior, next)
  }
}
