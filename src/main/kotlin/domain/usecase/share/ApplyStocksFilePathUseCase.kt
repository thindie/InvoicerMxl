package domain.usecase.share

import domain.PathProvider
import domain.ShareRepository
import domain.entities.PathType
import javax.inject.Inject

class ApplyStocksFilePathUseCase @Inject constructor(private val shareRepository: ShareRepository) {
    operator fun invoke(pathProvider: PathProvider) {
        shareRepository.applyPath(pathProvider, pathType = PathType.MINIMUM_REQUIRED)
    }
}