package domain.entities.abstractions

import java.io.IOException

abstract class ResultRatingWriter {

    @Throws(IOException::class)
    abstract fun write()
}