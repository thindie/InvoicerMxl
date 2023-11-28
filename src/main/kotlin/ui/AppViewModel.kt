package ui

import data.util.ScopeProvider
import javax.inject.Inject


class AppViewModel @Inject constructor(private val scopeProvider: ScopeProvider) {

    fun onClickOpenLocalRating() {

    }

    fun onClickOpenMergingRating() {

    }

    fun onError() {

    }

    private fun onSuccessLocal() {
    }

    private fun onSuccessCentral() {
    }


    data class ModelState (
        val localFilePath: String? = null,
        val mergingFilePath: String? = null,
        val isLocalFileParsed: Boolean = false,
        val isMergingFileParsed: Boolean = false,
    )
    }

