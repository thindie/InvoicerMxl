package domain.usecase.invoice

import domain.ActionsRepository
import domain.Event
import domain.OperationState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOperationsUseCase @Inject constructor(private val actionsRepository: ActionsRepository) {
    operator fun invoke(): Flow<Event<OperationState>>{
        return actionsRepository.observeActionsResult()
    }
}