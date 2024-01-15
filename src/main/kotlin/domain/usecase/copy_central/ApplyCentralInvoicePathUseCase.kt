package domain.usecase.copy_central

import domain.CopyInvoiceRepository
import domain.PathProvider
import domain.entities.PathType
import javax.inject.Inject

class ApplyCentralInvoicePathUseCase @Inject constructor(private val actionsRepository: CopyInvoiceRepository) {
    operator fun invoke(pathProvider: PathProvider) {
        actionsRepository.applyPath(pathProvider, pathType = PathType.MINIMUM_REQUIRED)
    }
}