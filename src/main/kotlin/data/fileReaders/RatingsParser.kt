package data.fileReaders;

import data.fileReaders.RatingsParser.Companion.replaceIndiciesFromStart
import domain.entities.Good
import domain.entities.GoodTitle
import domain.entities.emptyGood
import java.util.*

class RatingsParser {
    companion object {
        fun fromRating(text: String): Good? {
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
            } catch (e: Exception) {
                println(e.toString())
                println(text)
                emptyGood
            }
        }

        fun fromInvoice(text: String): Good? {
            return try {
                val vendorCode = text
                    .substring(text.indexOf("ЦБ"), text.indexOf("ЦБ") + 8)
                println(vendorCode)
                Good(vendor_code = vendorCode, rank = 0, sales = 0, stock = 0)
            } catch (e: Exception) {
                println(e.toString())
                println(text)
                emptyGood
            }
        }


        fun fromInventarisation(line: String): String {
            val title = line
                .replaceAfter(":", replacement = " ").trim()
                .replace(":", " ").trim()
                .replaceIndiciesFromStart()

            return title
        }


        fun fromStocks(line: String): GoodTitle {
            val title = line
                .replaceAfter(":", replacement = "").trim()
                .replace(":", " ").trim()
                .replaceBefore(" ", replacement = "").trimStart()
            return GoodTitle(title)
        }


        private fun String.replaceIndiciesFromStart(): String {

            return replaceBeforeLast("\t", " ").trim()
        }
    }
}

