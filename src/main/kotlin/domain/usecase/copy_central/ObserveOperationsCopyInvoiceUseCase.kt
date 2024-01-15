package domain.usecase.copy_central

import domain.CopyInvoiceRepository
import domain.Event
import domain.OperationState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOperationsCopyInvoiceUseCase @Inject constructor(private val actionsRepository: CopyInvoiceRepository) {
    operator fun invoke(): Flow<Event<OperationState>>{
        return actionsRepository.observeActionsResult()
    }
}