package data

import data.util.DispatcherProvider
import data.util.ScopeProvider
import data.util.implementations.InvoiceParser
import data.util.implementations.OperationStateFabric
import data.util.implementations.ShareParser
import domain.Event
import domain.OperationState
import domain.PathProvider
import domain.ShareRepository
import domain.entities.PathType
import domain.entities.SparePart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ShareOperatorImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val scopeProvider: ScopeProvider
) : ShareRepository {

    private val actionsState =
        MutableStateFlow(InvoiceOperationsState(OperationStateFabric.awaits()))


    override fun observeActionsResult(): Flow<Event<OperationState>> {
        return actionsState.filterNotNull().flowOn(dispatcherProvider.getDispatcher())
    }

    override fun applyPath(pathProvider: PathProvider, pathType: PathType) {
        when (pathType) {
            PathType.MINIMUM_REQUIRED -> actionsState.update {
                it.copy(initialPath = pathProvider.getProperty())
            }

            PathType.EXTENDED_OPERATION -> {
                /*ignore*/
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
        val onCompletion: (Boolean) -> Unit = { isSucceed ->
            if (isSucceed) stateSuccess()
            else stateError()
        }
        stateLoading()
        if (actionsState.value.isReady) {
            scopeProvider.getScope().launch(dispatcherProvider.getDispatcher()) {
                val incomingFile = readFile(actionsState.value.initialPath)
                val parsedFile: String = mapToSparePartsList(incomingFile)
                writeFile(actionsState.value.finalPath, parsedFile, onCompletion)
                stateStandBy()
            }

        } else stateError()

    }

    private fun mapToSparePartsList(incomingFile: List<String>): String {
        var isHaveApplyingTemplate = false
        return incomingFile.mapNotNull { line ->
            if (isHaveApplyingTemplate.not()) {
                if (line.contains(ShareParser.Properties.applyingTemplate.toRegex())) {
                    isHaveApplyingTemplate = true
                    fromLineToHeader(line)
                } else {
                    null
                }
            } else {
                val stock = fromLineToStock(line)
                if (stock.partNumber.isBlank()) null else stock
            }
        }
            .joinToString(separator = ShareParser.parserNextLine) {
                it.partNumber
                    .plus(ShareParser.parserDivider)
                    .plus(it.brand)
                    .plus(ShareParser.parserDivider)
                    .plus(it.quantity)
                    .plus(ShareParser.parserDivider)
                    .plus(it.price)
                    .plus(ShareParser.parserDivider)
            }
    }

    private fun fromLineToStock(line: String): SparePart {

        val fromLine = line.split("\t")
            .filter { it.isNotBlank() }

        return try {
            if (fromLine[1].contains(":").not()) error("")
            val partNumber = fromLine[1].getPartNumber()
            val brand = fromLine[1].getBrand()
            SparePart(
                partNumber = partNumber,
                brand = brand,
                quantity = fromLine[2].validateQuantity(),
                price = fromLine[3].validatePrice()
            )
        } catch (_: Exception) {
            fromLine.toString()
            SparePart("", "", "", "")
        }
    }

    private fun fromLineToHeader(line: String): SparePart {
        return SparePart(partNumber = ShareParser.parserHeader, price = "", quantity = "")
    }

    private suspend fun writeFile(finalPath: String, incomingFile: String, onCompletion: (Boolean) -> Unit) {
        withContext(dispatcherProvider.getDispatcher()) {
            try {
                Files.writeString(
                    Files.createFile(Path.of(newName(finalPath))),
                    incomingFile,
                    Charset.forName(InvoiceParser.PropertiesSupplier.parseCharset)
                )
            } catch (_: java.lang.Exception) {
                onCompletion(false)
            }
        }
    }

    private suspend fun readFile(initialPath: String): List<String> {
        return withContext(dispatcherProvider.getDispatcher()) {
            try {
                Files
                    .readAllLines(
                        Path.of(initialPath),
                        Charset.forName(ShareParser.Properties.charset)
                    )
            } catch (e: IOException) {
                stateError()
                emptyList()
            }
        }
    }

    private fun newName(finalPath: String) =
        finalPath
            .replaceAfterLast("\\", "")
            .plus(ShareParser.parserFileTitle)
            .plus(System.currentTimeMillis().toString().take(8))
            .plus(ShareParser.parserSuffix)


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
        val finalPath: String = ""
    ) : Event<OperationState> {
        override fun extract() = operationState

        val isReady = finalPath.isNotBlank() && initialPath.isNotBlank()

    }

}

private fun String.getPartNumber(): String {
    return substringBeforeLast(ShareParser.Properties.applyingTemplate)
        .trim()
}

private fun String.validatePrice(): String {
    return if (contains(".")) replaceAfterLast(".", "")
        .replace(".", "")
        .trim() else this
}

private fun String.validateQuantity(): String {
    return if (contains("\\.000".toRegex())) this else "1.000"
}

private fun String.getBrand(): String {
    return substringAfter(":")
        .split(" ")[1]
}