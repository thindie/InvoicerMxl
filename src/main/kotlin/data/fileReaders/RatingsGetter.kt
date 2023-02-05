package data.fileReaders;

import domain.GoodParserRepository
import domain.entities.Good
import domain.entities.abstractions.RawRatingReader
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**просто читает ТХТ "@param 1C отчет Рейтинг продаваемых товаров"*/

class RatingsGetter(private val fileNameIncoming: String) : RawRatingReader() {

    private fun fromFile(fileName: String): List<String> {
        try {
            return Files.readAllLines(Path.of(fileName), Charset.forName("Windows-1251"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList()
    }


    override fun read(): List<Good> {
        val goodList = mutableListOf<Good>()
        fromFile(fileNameIncoming).toMutableList().map {
            parseText(it)?.let { good -> goodList.add(good) }
        }
        return goodList.toList()
    }

    private fun parseText(text: String): Good? {
        val result: Good? = try {
            val data = Arrays.asList(*text.split("\\t".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
            val vendorCode = text
                .substring(text.indexOf("ЦБ"), text.indexOf("ЦБ") + 8)
            val rank = data[0].trim { it <= ' ' }
                .replace("\\.[0-9]00".toRegex(), "").toInt()
            val sales = data[data.size - 2].trim { it <= ' ' }
                .replace("\\.[0-9]00".toRegex(), "").toInt()
            val stock = data[data.size - 1].trim { it <= ' ' }
                .replace("\\.[0-9]00".toRegex(), "").toInt()
            val good = Good(vendor_code = vendorCode, rank = rank, sales = sales, stock = stock)
            good
        } catch (e: Exception) {
            println(e.toString())
            println(text)
            return null
        }
        return result
    }

    companion object {
        fun inject(fileName: String, goodParserRepository: GoodParserRepository) {
            goodParserRepository.parseFileRating = RatingsGetter(fileName)
        }
    }
}
