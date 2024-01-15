package domain.usecase.share

import domain.ShareRepository
import javax.inject.Inject

class RequestShareUseCase @Inject constructor(private val shareRepository: ShareRepository) {
    operator fun invoke() {
        shareRepository.requestAction()
    }
}