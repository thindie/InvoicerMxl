package domain.usecase.invoice

import domain.ActionsRepository
import javax.inject.Inject

class RequestActionsUseCase @Inject constructor(private val actionsRepository: ActionsRepository) {
    operator fun invoke() {
        actionsRepository.requestAction()
    }
}