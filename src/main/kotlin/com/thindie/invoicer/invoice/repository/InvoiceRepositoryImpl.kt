package com.thindie.invoicer.invoice.repository

import com.thindie.invoicer.application.error.AppError
import com.thindie.invoicer.domain.SparePart
import com.thindie.invoicer.domain.emptySparePart
import com.thindie.invoicer.invoice.repository.InvoiceParser.Anchor.LENGTH
import com.thindie.invoicer.invoice.repository.InvoiceParser.Anchor.MAX_VALUE
import com.thindie.invoicer.invoice.repository.InvoiceParser.Anchor.SECOND_CHAR
import com.thindie.invoicer.invoice.repository.InvoiceParser.CHARSET_WINDOWS_1251
import com.thindie.invoicer.invoice.repository.InvoiceParser.PARSE_SCHEMA_SIZE
import com.thindie.invoicer.invoice.repository.InvoiceParser.PARSE_SCHEMA_SUFFIX
import com.thindie.invoicer.invoice.repository.InvoiceParser.RESULT_PREFIX
import com.thindie.invoicer.invoice.repository.InvoiceParser.RESULT_TIMESTAMP_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class InvoiceRepositoryImpl(
  val filename: String,
  val filePath: File,
) : InvoiceRepository {

  private val mergeShemaCache = MutableStateFlow<String?>(null)

  override suspend fun readFileLines(path: String): List<String> {
	return withContext(Dispatchers.IO) {
	  try {
		configureInMemory1CBinaryOutputCacheInternal()
		return@withContext readFileLinesInternal(path)
	  } catch (e: IllegalStateException) {
		throw AppError.FilePathError(e.cause, e.message)
	  } catch (e: IOException) {
		throw AppError.FileReadError(e.cause, e.message)
	  }
	}
  }

  private fun configureInMemory1CBinaryOutputCacheInternal() {
	val cache = mergeShemaCache.value
	if (cache == null) {
	  val file = filePath.resolve(filename)
	  val schemaString = Files.readString(
		Paths.get(file.toURI()),
		Charset.forName(CHARSET_WINDOWS_1251)
	  )
	  mergeShemaCache.value = schemaString
	}
  }

  override suspend fun writeSimpleChildInvoice(inputPath: String, outputPath: String): InvoiceSummary {
	val result = outputPath to withContext(Dispatchers.IO) {
	  val saleRating = readFileLinesInternal(inputPath)
	  val goods = try {
		saleRating
		  .ifEmpty { null }
		  ?.mapNotNull { fromRating(it) } ?: error("Рейтинг товаров не должен быть пустым")
	  } catch (e: IllegalStateException) {
		throw AppError.MalformedSaleRatingError(e.cause, e.message)
	  }
	  try {
		write(
		  spareParts = goods,
		  limit = PARSE_SCHEMA_SIZE,
		  path = outputPath,
		)
	  } catch (e: Throwable) {
		throw AppError.FileWriteError(e.cause, e.message)
	  }
	}
	return InvoiceSummary(result)
  }

  override suspend fun writeMergeBranchInvoice(
	inputPathParent: String,
	inputPathChild: String,
	outputPath: String,
	limit: Int?,
	offset: Int,
  ): InvoiceSummary = withContext(Dispatchers.IO) {
	val childGoodsSet = readFileLinesInternal(inputPathChild)
	  .mapNotNull { extractGoodPartNumber(it) }
	  .toSet()

	val allGoods = readFileLinesInternal(inputPathParent)
	  .also {
		if (it.size < offset) throw AppError.WrongPreconditionsRequested(
		  null, "Offset bigger than goods size"
		)
	  }
	  .drop(offset)
	  .mapNotNull { line ->
		val parentPartNumber = extractGoodPartNumber(line) ?: return@mapNotNull null
		if (parentPartNumber in childGoodsSet) {
		  return@mapNotNull null
		}
		val good = fromRating(line)
		good.takeIf { (it?.stock ?: 0) > 0 }
	  }

	val summaryGoods = if (limit != null) allGoods.take(limit) else allGoods

	if (summaryGoods.isEmpty()) {
	  return@withContext InvoiceSummary(outputPath to 0)
	}
	try {
	  val count = write(
		spareParts = summaryGoods,
		limit = PARSE_SCHEMA_SIZE,
		path = outputPath,
	  )
	  InvoiceSummary(outputPath to count)
	} catch (e: Throwable) {
	  throw AppError.FileWriteError(e.cause, e.message)
	}
  }

  private fun splitGoodsInternal(
	initialList: List<SparePart>,
	limit: Int
  ): List<List<SparePart>> {
	return buildList {
	  val chunkQuota = initialList.size.div(limit).plus(1)
	  var currentIndex = 0
	  repeat(chunkQuota) {
		val list = mutableListOf<SparePart>()
		repeat(limit) {
		  try {
			list.add(initialList[currentIndex])
		  } catch (_: IndexOutOfBoundsException) {
			add(list)
			return this.toList()
		  }
		  currentIndex++
		}
		add(list)
	  }
	}
  }

  private fun readFileLinesInternal(path: String): List<String> {
	return try {
	  Files.readAllLines(
		Path.of(path),
		Charset.forName(CHARSET_WINDOWS_1251)
	  )
	} catch (e: IOException) {
	  throw AppError.FileReadError(e.cause, e.message)
	}
  }

  private fun replaceVendorCodes(invoice: List<SparePart>, schema: String): String {
	var resultString = schema
	val anchorStartIndices = mutableListOf<Int>()
	val stringMassive = Arrays.asList(
	  *schema.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
	)
	for (i in stringMassive.indices) {
	  if (i + InvoiceParser.ITERATIONS < stringMassive.size &&
		stringMassive[i] == InvoiceParser.Anchor.FIRST_CHAR &&
		stringMassive[i + InvoiceParser.ITERATIONS] == SECOND_CHAR
	  ) {
		anchorStartIndices.add(i - InvoiceParser.ITERATIONS)
	  }
	}
	var i = 0
	val limit = invoice.size.coerceAtMost(PARSE_SCHEMA_SIZE)
	while (i < limit) {
	  val schemaVendorCode = try {
		resultString.substring(
		  anchorStartIndices[i],
		  anchorStartIndices[i] + LENGTH
		)
	  } catch (_: IndexOutOfBoundsException) {
		null
	  }
	  if (schemaVendorCode != null) {
		val vendorCode = try {
		  invoice[i].vendorCode
		} catch (_: IndexOutOfBoundsException) {
		  MAX_VALUE // will become an empty string at the binary
		}
		resultString = resultString.replace(schemaVendorCode, vendorCode)
		i++
	  }
	}
	return resultString
  }

  private suspend fun write(
	path: String,
	spareParts: List<SparePart>,
	limit: Int
  ): Int {
	return withContext(Dispatchers.IO) {
	  val dividedList = splitGoodsInternal(spareParts, limit)
	  dividedList.forEachIndexed { times, dividedGoodsList ->
		val mergeSchema = mergeShemaCache.value
		if (mergeSchema == null) {
		  configureInMemory1CBinaryOutputCacheInternal()
		}
		val richSchema = replaceVendorCodes(dividedGoodsList, requireNotNull(mergeShemaCache.value))
		Files.writeString(
		  Files.createFile(Path.of(newName(times, path))),
		  richSchema,
		  Charset.forName(CHARSET_WINDOWS_1251)
		)
	  }
	  dividedList.size
	}
  }

  private fun fromRating(text: String): SparePart? {
	return try {
	  val data = Arrays
		.asList(*text.split("\\t".toRegex()).dropLastWhile { it.isEmpty() }
		  .toTypedArray())
	  val vendorCode = text
		.substring(text.indexOf("ЦБ"), text.indexOf("ЦБ") + 8)
	  val rank = data[0].trim { it <= ' ' }
		.replace("\\.[0-9]00".toRegex(), "").toInt()
	  val sales = data[data.size - 2].trim { it <= ' ' }
		.replace("\\.[0-9]00".toRegex(), "").toInt()
	  val stock = data[data.size - 1].trim { it <= ' ' }
		.replace("\\.[0-9]00".toRegex(), "").toInt()
	  SparePart(vendorCode = vendorCode, rank = rank, sales = sales, stock = stock)
	} catch (_: Exception) {
	  emptySparePart
	}
  }

  private fun extractGoodPartNumber(line: String): String? {
	val colonIndex = line.indexOf(':')
	if (colonIndex == -1) return null
	val tabIndex = line.lastIndexOf('\t', colonIndex)
	val start = if (tabIndex == -1) 0 else tabIndex + 1
	val result = line.substring(start, colonIndex).trim()
	return result.ifEmpty { null }
  }

  private fun newName(times: Int, fileName: String) =
	fileName.plus(
	  System.currentTimeMillis()
		.toString()
		.substring(RESULT_TIMESTAMP_LENGTH)
	)
	  .plus(RESULT_PREFIX)
	  .plus(times)
	  .plus(PARSE_SCHEMA_SUFFIX)
}
