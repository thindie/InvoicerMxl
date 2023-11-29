package domain

interface Event<T> {
    fun extract(): T
}