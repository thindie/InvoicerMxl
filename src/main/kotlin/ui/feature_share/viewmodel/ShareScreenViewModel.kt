package ui.feature_share.viewmodel

import data.util.ScopeProvider
import domain.PathProvider
import domain.entities.Engine
import domain.usecase.share.ApplyMappingStocksPathUseCase
import domain.usecase.share.ApplyStocksFilePathUseCase
import domain.usecase.share.ObserveMappingStateUseCase
import domain.usecase.share.RequestShareUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import root.ViewModel
import javax.inject.Inject

class ShareScreenViewModel @Inject constructor(
    private val scopeProvider: ScopeProvider,
    private val observeMappingStateUseCase: ObserveMappingStateUseCase,
    private val requestShareUseCase: RequestShareUseCase,
    private val applyStocksFilePathUseCase: ApplyStocksFilePathUseCase,
    private val applyMappingStocksPathUseCase: ApplyMappingStocksPathUseCase
) : ViewModel {

    private val _operationsState = MutableStateFlow(ModelState(state = Engine.STANDBY))

    val operationsState =
        _operationsState.combine(observeMappingStateUseCase.invoke()) { modelState, operationsState ->
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

    fun onClickOpenLocalStocks(path: String) {
        _operationsState.update {
            it.copy(localFilePath = path)
        }
        scopeProvider.getScope().launch {
            applyStocksFilePathUseCase.invoke(
                PathHolder(path)
            )
        }
    }

    fun onClickStartOperation(path: String) {
        scopeProvider.getScope().launch {
            applyMappingStocksPathUseCase.invoke(
                PathHolder(path)
            )
            requestShareUseCase()
        }
    }

    fun onDismissBasicPath() {
        _operationsState.update {
            it.copy(localFilePath = null)
        }
    }

    data class ModelState(
        val localFilePath: String? = null,
        val state: Engine
    )

    private data class PathHolder(
        val path: String
    ) : PathProvider {
        override fun getProperty() = path

    }

}