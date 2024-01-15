package domain.usecase.copy_central

import domain.CopyInvoiceRepository
import javax.inject.Inject

class RequestActionsCopyInvoiceUseCase @Inject constructor(private val actionsRepository: CopyInvoiceRepository) {
    operator fun invoke() {
        actionsRepository.requestAction()
    }
}