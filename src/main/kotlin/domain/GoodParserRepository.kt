package domain


import domain.entities.Good
import domain.entities.abstractions.GoodsVolume
import domain.entities.abstractions.RawRatingReader
import domain.entities.abstractions.ResultRatingWriter
import kotlinx.coroutines.flow.Flow


interface GoodParserRepository {
    var parseFileRating: RawRatingReader
    var writeFileRating: ResultRatingWriter
    suspend fun getAllGoods(fileName: String): Flow<List<Good>>
    suspend fun writeNewGoodsFile(fileName: String, goodsList: List<GoodsVolume>)
}