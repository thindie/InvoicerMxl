package data

import data.fileReaders.RatingsGetter
import data.fileWriters.RatingWriter
import domain.GoodParserRepository
import domain.entities.CentralBaseGoodsVolume
import domain.entities.Good
import domain.entities.LocalBaseGoodsVolume
import domain.entities.abstractions.GoodsVolume
import domain.entities.abstractions.RawRatingReader
import domain.entities.abstractions.ResultRatingWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object GoodParserRepositoryImpl : GoodParserRepository {
    override lateinit var parseFileRating: RawRatingReader
    override lateinit var writeFileRating: ResultRatingWriter


    override suspend fun getAllGoods(fileName: String): Flow<List<Good>> {
        RatingsGetter.inject(fileName, this)

        return flow { emit(parseFileRating.read()) }
    }

    override suspend fun writeNewGoodsFile(fileName: String, goodsList: List<GoodsVolume>) {
        var local: LocalBaseGoodsVolume? = null
        var central: CentralBaseGoodsVolume? = null

        goodsList.forEach {
            when (it) {
                is LocalBaseGoodsVolume -> {
                    local = it
                }

                is CentralBaseGoodsVolume -> {
                    central = it
                }
            }
        }
        RatingWriter.inject(
            fileName,
            this,
            buildMergedGoodsList(local ?: throw Exception(), central ?: throw Exception())
        )
        writeFileRating.write()

    }

    private fun buildMergedGoodsList(
        localRatingsList: LocalBaseGoodsVolume,
        allTimeRating: CentralBaseGoodsVolume,
    ): List<Good> {

        val mergedGoodsVolumeList = mutableListOf<Good>()

        val localListFiltered = localRatingsList.list.filter {
            it.stock == 0
        }
        localListFiltered.forEach { if (it.stock == 0) mergedGoodsVolumeList.add(it) }

        val allTimeListFiltered = allTimeRating.list.filter {
            !mergedGoodsVolumeList.contains(it)
        }

        allTimeListFiltered.forEach { if (it.stock > 0) mergedGoodsVolumeList.add(it) }

        return mergedGoodsVolumeList.toList()
    }
}