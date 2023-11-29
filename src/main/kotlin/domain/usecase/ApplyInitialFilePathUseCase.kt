package domain.usecase

import domain.ActionsRepository
import domain.PathProvider
import domain.entities.PathType
import javax.inject.Inject

class ApplyInitialFilePathUseCase @Inject constructor(private val actionsRepository: ActionsRepository) {
    operator fun invoke(pathProvider: PathProvider){
        actionsRepository.applyPath(pathProvider, pathType = PathType.MINIMUM_REQUIRED)
    }
}