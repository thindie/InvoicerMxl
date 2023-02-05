package domain.entities

import domain.entities.abstractions.GoodsVolume

data class LocalBaseGoodsVolume(override val list: List<Good>) : GoodsVolume()