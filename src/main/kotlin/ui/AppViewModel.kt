package ui

import data.util.ScopeProvider
import domain.PathProvider
import domain.usecase.ApplyInitialFilePathUseCase
import domain.usecase.ObserveOperationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppViewModel @Inject constructor(
    private val scopeProvider: ScopeProvider,
    private val observeOperationsUseCase: ObserveOperationsUseCase,
    private val applyInitialFilePathUseCase: ApplyInitialFilePathUseCase
) {

    private val _operationsState = MutableStateFlow(ModelState())

    val operationsState =
        _operationsState.combine(observeOperationsUseCase.invoke()) { modelState, operationsState ->
            val status = operationsState
                .extract()

            modelState.copy(
                isError = status.isError,
                isLoading = status.isLoading,
                isStandBy = status.standBy,
                isSuccess = status.isSuccess
            )
        }
            .stateIn(
                scope = scopeProvider.getScope(),
                initialValue = ModelState(),
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

    fun onClickOpenMergingRating() {

    }

    fun onClickStartOperation() {

    }

    data class ModelState(
        val localFilePath: String? = null,
        val mergingFilePath: String? = null,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val isError: Boolean = false,
        val isStandBy: Boolean = true
    )

    private data class PathHolder(
        val path: String
    ) : PathProvider {
        override fun getProperty() = path

    }
}

