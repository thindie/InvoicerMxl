package data.fileWriters


import domain.entities.Good
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

private const val MXL = ".mxl"
private const val INVOICE = "_inv_"
private const val CUT_TIMEMILLIS = 7
const val DLL = "st.dll"
private const val DLL_SIZE = 99
const val CHARSET = "Windows-1251"
private const val FIRST_ANCHOR = "Ц"
private const val SECOND_ANCHOR = "Б"
private const val MOCK_THING = "ЦБ999999"
private const val FAKE_ARG = 0
private const val ANCHOR_SIZE = 8
private const val ITERATE = 1


class RatingWriter(private val allIncomingGoodslist: List<Good>, private val fileName: String) : ResultRatingWriter() {
    private val resourcesDir = File(System.getProperty("compose.application.resources.dir"))

    @Throws(IOException::class)
    override fun write() {

        for (timesDueToGoodsToInvoice in 0..allIncomingGoodslist.size.div(DLL_SIZE)) {
            val createListDueToDLL = calculateDependOnIncomingList(timesDueToGoodsToInvoice, allIncomingGoodslist)
            val invoiceFile = doingAWorkWithListAndSkeleton(createListDueToDLL, createSkeletonOfFile())
            try {
                Files.writeString(
                    Files.createFile(Path.of(newName(timesDueToGoodsToInvoice))), invoiceFile, Charset.forName(CHARSET)
                )
            } catch (e: FileAlreadyExistsException) {
                Files.writeString(
                    Files.createFile(Path.of(newName(timesDueToGoodsToInvoice))), invoiceFile, Charset.forName(CHARSET)
                )
            }
        }
    }

    private fun newName(times: Int) =
        fileName.plus(System.currentTimeMillis().toString().substring(CUT_TIMEMILLIS)).plus(INVOICE).plus(times)
            .plus(MXL)


    private val createSkeletonOfFile: () -> String = {
        val resource = resourcesDir.resolve(DLL)
        Files.readString(
            Paths.get(
                resource.toURI() ?: throw NullPointerException()
            ), Charset.forName(CHARSET)
        )

    }

    private fun doingAWorkWithListAndSkeleton(listGoodsToInvoice: List<Good>, emptyVersionOfFile: String): String {
        println("SERVICE_TAG ${allIncomingGoodslist[0].vendor_code}")
        var resultString = emptyVersionOfFile
        val massiveOfNumbersOfNomeToReplace: MutableList<Int> = mutableListOf()
        //массив индексов, с которых начинаются ЦБ в файле (сука хитро)
        val stringMassive = Arrays.asList(*emptyVersionOfFile.split("".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) //emptyversionoffile посимвольно
        for (i in stringMassive.indices) {
            if (stringMassive[i] == FIRST_ANCHOR && stringMassive[i + ITERATE] == SECOND_ANCHOR) massiveOfNumbersOfNomeToReplace.add(
                i - ITERATE
            )
        }


        // CB to Num Replace
        var i = 0
        while (i < listGoodsToInvoice.size && i < DLL_SIZE) {
            val target = resultString.substring(
                massiveOfNumbersOfNomeToReplace[i], massiveOfNumbersOfNomeToReplace[i] + ANCHOR_SIZE
            )
            val vendorCode = try {
                listGoodsToInvoice[i].vendor_code
            } catch (e: IndexOutOfBoundsException) {
                MOCK_THING
            }
            resultString = resultString.replace(target, vendorCode)
            i++
        }
        return resultString
    }// ебать хитрО

    private fun calculateDependOnIncomingList(times: Int, list: List<Good>): List<Good> {
        println("$times")
        val firstIndex = times.times(DLL_SIZE)
        val secondIndex = firstIndex.plus(DLL_SIZE)
        println("$firstIndex $secondIndex")
        val listToReturn = mutableListOf<Good>()
        try {
            list[secondIndex]
        } catch (e: IndexOutOfBoundsException) {
            for (i in firstIndex until list.size) {
                listToReturn.add(list[i])

            }
            do {
                listToReturn.add(Good(FAKE_ARG, FAKE_ARG, MOCK_THING, FAKE_ARG))
            } while (listToReturn.size < DLL_SIZE)

            return listToReturn.toList()
        }
        listToReturn.addAll(list.subList(firstIndex, secondIndex))
        return listToReturn.toList()
    }
}


