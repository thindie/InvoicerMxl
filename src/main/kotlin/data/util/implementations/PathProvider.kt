package data.util.implementations

import data.util.SystemPropertyPathProvider

internal class PathProvider(private val systemProperty: String): SystemPropertyPathProvider {
    override fun getProperty() = systemProperty
}