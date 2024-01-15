package ui.feature_from_from_central_invoice.viewmodel

import data.util.ScopeProvider
import domain.PathProvider
import domain.entities.Engine
import domain.usecase.copy_central.ApplyCentralInvoicePathUseCase
import domain.usecase.copy_central.ApplyInventarisationPathUseCase
import domain.usecase.copy_central.ApplyResultCopyInvoiceUseCase
import domain.usecase.copy_central.ObserveOperationsCopyInvoiceUseCase
import domain.usecase.copy_central.RequestActionsCopyInvoiceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import root.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvoiceScreenCentralBaseViewModel @Inject constructor(
    private val scopeProvider: ScopeProvider,
    private val observeOperationsUseCase: ObserveOperationsCopyInvoiceUseCase,
    private val applyInitialFilePathUseCase: ApplyCentralInvoicePathUseCase,
    private val applyAdditionFilePathUseCase: ApplyInventarisationPathUseCase,
    private val applyResult: ApplyResultCopyInvoiceUseCase,
    private val requestActionsUseCase: RequestActionsCopyInvoiceUseCase
) : ViewModel {

    private val _operationsState = MutableStateFlow(ModelState(state = Engine.STANDBY))

    val operationsState =
        _operationsState.combine(observeOperationsUseCase.invoke()) { modelState, operationsState ->
            val status = operationsState
                .extract()

            modelState.copy(
                state = status.currentState
            )
        }
            .stateIn(
                scope = scopeProvider.getScope(),
                initialValue = ModelState(state = Engine.STANDBY),
                started = SharingStarted.WhileSubscribed(5_000L)
            )

    fun onClickOpenCentralBaseInvoice(path: String) {
        _operationsState.update {
            it.copy(localFilePath = path)
        }
        scopeProvider.getScope().launch {
            applyInitialFilePathUseCase.invoke(
                PathHolder(path)
            )
        }
    }

    fun onClickOpenInventarisation(path: String) {
        _operationsState.update {
            it.copy(mergingFilePath = path)
        }
        scopeProvider.getScope().launch {
            applyAdditionFilePathUseCase.invoke(
                PathHolder(path)
            )
        }
    }

    fun onClickStartOperation(path: String) {
        scopeProvider.getScope().launch {
            println("onStart")
            applyResult.invoke(
                PathHolder(path)
            )
            requestActionsUseCase()
        }
    }

    fun onDismissBasicPath() {
        _operationsState.update {
            it.copy(localFilePath = null)
        }
    }

    fun onDismissExtraPath() {
        _operationsState.update {
            it.copy(mergingFilePath = null)
        }
    }

    data class ModelState(
        val localFilePath: String? = null,
        val mergingFilePath: String? = null,
        val state: Engine
    )

    private data class PathHolder(
        val path: String
    ) : PathProvider {
        override fun getProperty() = path

    }
}

