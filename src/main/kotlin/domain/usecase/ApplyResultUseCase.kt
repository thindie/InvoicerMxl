package domain.usecase

import domain.ActionsRepository
import domain.PathProvider
import domain.entities.PathType
import javax.inject.Inject

class ApplyResultUseCase @Inject constructor(private val actionsRepository: ActionsRepository) {
    operator fun invoke(pathProvider: PathProvider){
        actionsRepository.applyPath(pathProvider, pathType = PathType.SAVING_PATH)
    }
}