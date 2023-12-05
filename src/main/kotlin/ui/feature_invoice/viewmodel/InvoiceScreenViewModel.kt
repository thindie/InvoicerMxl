package ui.feature_invoice.viewmodel

import data.util.ScopeProvider
import domain.PathProvider
import domain.entities.Engine
import domain.usecase.ApplyAdditionFilePathUseCase
import domain.usecase.ApplyInitialFilePathUseCase
import domain.usecase.ApplyResultUseCase
import domain.usecase.ObserveOperationsUseCase
import domain.usecase.RequestActionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvoiceScreenViewModel @Inject constructor(
    private val scopeProvider: ScopeProvider,
    private val observeOperationsUseCase: ObserveOperationsUseCase,
    private val applyInitialFilePathUseCase: ApplyInitialFilePathUseCase,
    private val applyAdditionFilePathUseCase: ApplyAdditionFilePathUseCase,
    private val applyResult: ApplyResultUseCase,
    private val requestActionsUseCase: RequestActionsUseCase
) {

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

    fun onClickOpenLocalRating(path: String) {
        _operationsState.update {
            it.copy(localFilePath = path)
        }
        scopeProvider.getScope().launch {
            applyInitialFilePathUseCase.invoke(
                PathHolder(path)
            )
        }
    }

    fun onClickOpenMergingRating(path: String) {
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

