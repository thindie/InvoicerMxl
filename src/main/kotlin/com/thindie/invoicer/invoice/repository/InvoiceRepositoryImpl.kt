package com.thindie.invoicer.invoice.repository

import com.thindie.invoicer.application.error.AppError
import data.util.implementations.InvoiceParser
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.anchorLength
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.mockGood
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.secondAnchor
import data.util.implementations.InvoiceParser.PropertiesSupplier.cutTimeMillis
import data.util.implementations.InvoiceParser.PropertiesSupplier.parseCharset
import data.util.implementations.InvoiceParser.PropertiesSupplier.parseSchemaSize
import data.util.implementations.InvoiceParser.PropertiesSupplier.resultFilePrefix
import data.util.implementations.InvoiceParser.PropertiesSupplier.resultFileSuffix
import domain.entities.Good
import domain.entities.emptyGood
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
		Charset.forName(parseCharset)
	  )
	  mergeShemaCache.value = schemaString
	}
  }

  override suspend fun writeSimpleChildInvoice(inputPath: String, outputPath: String) {
	withContext(Dispatchers.IO) {
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
		  goods = goods,
		  limit = parseSchemaSize,
		  path = outputPath,
		)
	  } catch (e: Throwable) {
		throw AppError.FileWriteError(e.cause, e.message)
	  }
	}
  }

  private fun splitGoodsInternal(
	initialList: List<Good>,
	limit: Int
  ): List<List<Good>> {
	return buildList {
	  val chunkQuota = initialList.size.div(limit).plus(1)
	  var currentIndex = 0
	  repeat(chunkQuota) {
		val list = mutableListOf<Good>()
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
		Charset.forName(parseCharset)
	  )
	} catch (e: IOException) {
	  throw AppError.FileReadError(e.cause, e.message)
	}
  }

  private fun replaceVendorCodes(invoice: List<Good>, schema: String): String {
	var resultString = schema
	val anchorStartIndices = mutableListOf<Int>()
	val stringMassive = Arrays.asList(
	  *schema.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
	)
	for (i in stringMassive.indices) {
	  if (i + InvoiceParser.PropertiesSupplier.iterate < stringMassive.size &&
		stringMassive[i] == InvoiceParser.PropertiesSupplier.Good.startAnchor &&
		stringMassive[i + InvoiceParser.PropertiesSupplier.iterate] == secondAnchor
	  ) {
		anchorStartIndices.add(i - InvoiceParser.PropertiesSupplier.iterate)
	  }
	}
	var i = 0
	val limit = invoice.size.coerceAtMost(parseSchemaSize)
	while (i < limit) {
	  val schemaVendorCode = try {
		resultString.substring(
		  anchorStartIndices[i],
		  anchorStartIndices[i] + anchorLength
		)
	  } catch (_: IndexOutOfBoundsException) {
		null
	  }
	  if (schemaVendorCode != null) {
		val vendorCode = try {
		  invoice[i].vendor_code
		} catch (_: IndexOutOfBoundsException) {
		  mockGood // will become an empty string at the binary
		}
		resultString = resultString.replace(schemaVendorCode, vendorCode)
		i++
	  }
	}
	return resultString
  }

  private suspend fun write(
	path: String,
	goods: List<Good>,
	limit: Int
  ) {
	withContext(Dispatchers.IO) {
	  val dividedList = splitGoodsInternal(goods, limit)
	  dividedList.forEachIndexed { times, dividedGoodsList ->
		val mergeSchema = mergeShemaCache.value
		if (mergeSchema == null) {
		  configureInMemory1CBinaryOutputCacheInternal()
		}
		val richSchema = replaceVendorCodes(dividedGoodsList, requireNotNull(mergeShemaCache.value))
		Files.writeString(
		  Files.createFile(Path.of(newName(times, path))),
		  richSchema,
		  Charset.forName(parseCharset)
		)
	  }
	}
  }

  private fun fromRating(text: String): Good? {
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
	  Good(vendor_code = vendorCode, rank = rank, sales = sales, stock = stock)
	} catch (_: Exception) {
	  emptyGood
	}
  }

  private fun newName(times: Int, fileName: String) =
	fileName.plus(
	  System.currentTimeMillis().toString().substring(cutTimeMillis)
	).plus(resultFilePrefix).plus(times).plus(resultFileSuffix)
}
