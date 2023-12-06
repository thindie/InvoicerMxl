package domain.usecase.share

import domain.Event
import domain.OperationState
import domain.ShareRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMappingStateUseCase @Inject constructor(private val shareRepository: ShareRepository) {
    operator fun invoke(): Flow<Event<OperationState>> {
        return shareRepository.observeActionsResult()
    }
}