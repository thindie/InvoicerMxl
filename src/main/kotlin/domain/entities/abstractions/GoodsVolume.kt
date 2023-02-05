package domain.entities.abstractions

import domain.entities.Good

abstract class GoodsVolume {
    abstract val list: List<Good>
}