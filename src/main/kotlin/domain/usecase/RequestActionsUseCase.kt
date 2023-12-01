package domain.usecase

import domain.ActionsRepository
import javax.inject.Inject

class RequestActionsUseCase @Inject constructor(private val actionsRepository: ActionsRepository) {
    operator fun invoke() {
        actionsRepository.requestAction()
    }
}