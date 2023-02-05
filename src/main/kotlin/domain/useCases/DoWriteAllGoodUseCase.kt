package domain.useCases

import domain.GoodParserRepository
import domain.entities.abstractions.GoodsVolume

class DoWriteAllGoodUseCase(private val goodParserRepository: GoodParserRepository) : UseCase() {
    suspend operator fun invoke(fileName: String, goodsList: List<GoodsVolume>) {
        goodParserRepository.writeNewGoodsFile(fileName, goodsList)
    }
}
