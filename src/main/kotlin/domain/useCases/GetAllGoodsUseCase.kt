package domain.useCases

import domain.GoodParserRepository
import domain.entities.Good
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllGoodsUseCase(private val goodParserRepository: GoodParserRepository) : UseCase() {
    suspend operator fun invoke(fileName: String): Flow<List<Good>> {
        return goodParserRepository.getAllGoods(fileName).flowOn(Dispatchers.IO)
    }
}