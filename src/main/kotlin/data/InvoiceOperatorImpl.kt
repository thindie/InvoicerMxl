package data


import data.fileReaders.RatingsParser
import data.util.DispatcherProvider
import data.util.ScopeProvider
import data.util.SystemPropertyPathProvider
import data.util.implementations.InvoiceParser
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.anchorLength
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.mockGood
import data.util.implementations.InvoiceParser.PropertiesSupplier.Good.secondAnchor
import data.util.implementations.InvoiceParser.PropertiesSupplier.cutTimeMillis
import data.util.implementations.InvoiceParser.PropertiesSupplier.parseCharset
import data.util.implementations.InvoiceParser.PropertiesSupplier.parseSchemaSize
import data.util.implementations.InvoiceParser.PropertiesSupplier.resultFilePrefix
import data.util.implementations.InvoiceParser.PropertiesSupplier.resultFileSuffix
import data.util.implementations.OperationStateFabric
import domain.ActionsRepository
import domain.Event
import domain.OperationState
import domain.PathProvider
import domain.entities.Good
import domain.entities.PathType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class InvoiceOperatorImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val scopeProvider: ScopeProvider,
    private val resourceDir: File,
    @Named("skeleton") private val skeletonProperty: SystemPropertyPathProvider
) : ActionsRepository {

    private val actionsState = MutableStateFlow(InvoiceOperationsState(OperationStateFabric.awaits()))

    private lateinit var mergeSchema: String


    init {
        scopeProvider
            .getScope()
            .launch {
                val resource = resourceDir.resolve(skeletonProperty.getProperty())
                mergeSchema = withContext(dispatcherProvider.getDispatcher()) {
                    try {
                        Files.readString(
                            Paths.get(
                                resource
                                    .toURI() ?: error("")
                            ), Charset.forName(parseCharset)
                        )
                    } catch (e: IllegalStateException) {
                        stateError()
                        ""
                    }
                }
            }
    }


    override fun observeActionsResult(): Flow<Event<OperationState>> {
        return actionsState
            .filterNotNull()
            .flowOn(dispatcherProvider.getDispatcher())
    }

    override fun applyPath(pathProvider: PathProvider, pathType: PathType) {
        when (pathType) {
            PathType.MINIMUM_REQUIRED -> {
                actionsState.update {
                    it.copy(initialPath = pathProvider.getProperty())
                }
            }

            PathType.EXTENDED_OPERATION -> {
                actionsState.update {
                    it.copy(extendedPath = pathProvider.getProperty())
                }
            }

            PathType.SAVING_PATH -> {
                actionsState.update {
                    it.copy(finalPath = pathProvider.getProperty())
                }
            }

            PathType.SYSTEM_INSTRUCTIONS -> {
                /*ignore*/
            }
        }
    }

    override fun requestAction() {
        scopeProvider.getScope().launch {
            stateLoading()
            var isError = false
            if (actionsState.value.isOnlyLocalInvoice()) {
                val requestedRatingGet = readFile(actionsState.value.initialPath)
                val parseIntoEntitiesList = requestedRatingGet
                    .mapNotNull { RatingsParser.fromRating(it) }
                    .filter { it.stock == 0 }
                    .apply {
                        if (isEmpty()) stateError()
                    }
                operateGoodsList(
                    parseIntoEntitiesList = parseIntoEntitiesList,
                    entitiesLimit = parseSchemaSize,
                )
            } else if (actionsState.value.isMergingInvoice()) {
                val localStocksFile = readFile(actionsState.value.initialPath)
                val parseIntoGoodsTitleList = localStocksFile.map {
                    RatingsParser.fromStocks(it)
                }

                val centralRating = readFile(actionsState.value.extendedPath)
                    .filterNot {
                        val title = RatingsParser.fromStocks(it)
                        println(title)
                        parseIntoGoodsTitleList.contains(title)
                    }.mapNotNull {
                        RatingsParser.fromRating(it)
                    }.filter {
                        it.stock != 0
                    }

                operateGoodsList(parseIntoEntitiesList = centralRating, entitiesLimit = parseSchemaSize)

            } else {
                isError = true
            }

            if (isError.not()) {
                stateStandBy()
            }
        }
    }

    private fun divideGoodsListBySkeletonFileLimit(initialList: List<Good>, limit: Int): List<List<Good>> {
        return buildList<List<Good>> {
            val repetitions = initialList.size.div(limit).plus(1)
            var currentIndex = 0
            repeat(repetitions) {
                val list = mutableListOf<Good>()
                repeat(limit) {
                    try {
                        list.add(initialList[currentIndex])
                    } catch (_: Exception) {
                        this.add(list)
                        return this.toList()
                    }
                    currentIndex++
                }
                this.add(list)
            }
        }
    }

    private suspend fun readFile(initialPath: String): List<String> {
        return withContext(dispatcherProvider.getDispatcher()) {
            try {
                Files
                    .readAllLines(
                        Path.of(initialPath),
                        Charset.forName("Windows-1251")
                    )
            } catch (e: IOException) {
                stateError()
                emptyList()
            }
        }
    }


    private fun mergeGoodsListWithInvoiceSchema(listGoodsToInvoice: List<Good>, emptyVersionOfFile: String): String {
        var resultString = emptyVersionOfFile
        val massiveOfNumbersOfNomeToReplace: MutableList<Int> = mutableListOf()
        //массив индексов, с которых начинаются ЦБ в файле (сука хитро)
        val stringMassive = Arrays.asList(*emptyVersionOfFile.split("".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) //emptyversionoffile посимвольно
        for (i in stringMassive.indices) {
            if (stringMassive[i] == InvoiceParser.PropertiesSupplier.Good.startAnchor
                && stringMassive[i + InvoiceParser.PropertiesSupplier.iterate]
                == secondAnchor
            ) massiveOfNumbersOfNomeToReplace.add(
                i - InvoiceParser.PropertiesSupplier.iterate
            )
        }
        // CB to Num Replace
        var i = 0
        while (i < listGoodsToInvoice.size && i < parseSchemaSize) {
            val target = resultString.substring(
                massiveOfNumbersOfNomeToReplace[i],
                massiveOfNumbersOfNomeToReplace[i] + anchorLength
            )
            val vendorCode = try {
                listGoodsToInvoice[i].vendor_code
            } catch (e: IndexOutOfBoundsException) {
                mockGood
            }
            resultString = resultString.replace(target, vendorCode)
            i++
        }
        return resultString
    }

    private suspend fun operateGoodsList(
        parseIntoEntitiesList: List<Good>,
        entitiesLimit: Int,
    ) {
        withContext(dispatcherProvider.getDispatcher()) {
            val dividedList = divideGoodsListBySkeletonFileLimit(parseIntoEntitiesList, entitiesLimit)
            dividedList.forEachIndexed() { i, dividedGoodsList ->
                var isErrorProduced = false
                val invoiceFile = mergeGoodsListWithInvoiceSchema(dividedGoodsList, mergeSchema)
                writeFile(
                    stringToFile = invoiceFile,
                    fileName = actionsState.value.finalPath,
                    iteration = i,
                    notifyError = {
                        isErrorProduced = true
                    })
                if (isErrorProduced.not()) {
                    if (i == dividedList.size) {
                        stateSuccess()
                    } else {
                        stateLoading()
                    }
                }
            }
        }
    }

    private fun writeFile(stringToFile: String, fileName: String, iteration: Int, notifyError: () -> Unit) {
        try {
            Files.writeString(
                Files.createFile(Path.of(newName(iteration, fileName))), stringToFile, Charset.forName(parseCharset)
            )
        } catch (_: java.lang.Exception) {
            notifyError.invoke()
            stateError()
        }
    }

    private fun newName(times: Int, fileName: String) =
        fileName.plus(
            System
                .currentTimeMillis()
                .toString()
                .substring(cutTimeMillis)
        )
            .plus(resultFilePrefix).plus(times)
            .plus(resultFileSuffix)


    private fun stateError() {
        actionsState.update { it.copy(operationState = OperationStateFabric.error()) }
    }

    private fun stateLoading() {
        actionsState.update { it.copy(operationState = OperationStateFabric.loading()) }
    }

    private fun stateSuccess() {
        actionsState.update { it.copy(operationState = OperationStateFabric.success()) }
    }

    private fun stateStandBy() {
        actionsState.update { it.copy(operationState = OperationStateFabric.awaits()) }
    }

    data class InvoiceOperationsState(
        val operationState: OperationState,
        val initialPath: String = "",
        val extendedPath: String = "",
        val finalPath: String = ""
    ) : Event<OperationState> {
        override fun extract() = operationState

        fun isOnlyLocalInvoice() = initialPath.isNotBlank() && extendedPath.isBlank()
        fun isMergingInvoice() = initialPath.isNotBlank() && extendedPath.isNotBlank()
    }
}
