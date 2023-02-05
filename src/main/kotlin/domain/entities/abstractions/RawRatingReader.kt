package domain.entities.abstractions

import domain.entities.Good

abstract class RawRatingReader {
    abstract fun read(): List<Good>

}