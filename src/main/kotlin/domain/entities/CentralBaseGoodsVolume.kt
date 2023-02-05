package domain.entities

import domain.entities.abstractions.GoodsVolume

data class CentralBaseGoodsVolume(override val list: List<Good>) : GoodsVolume()