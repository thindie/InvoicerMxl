package ui

import domain.entities.CentralBaseGoodsVolume
import domain.entities.LocalBaseGoodsVolume
import domain.entities.abstractions.GoodsVolume
import domain.useCases.DoWriteAllGoodUseCase
import domain.useCases.GetAllGoodsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AppModel() : InvoicerUIRepository {
    override lateinit var writeAllGoods: DoWriteAllGoodUseCase
    override lateinit var getAllGoodsUseCase: GetAllGoodsUseCase
    val scope = CoroutineScope(Dispatchers.Default)

    private val _goodsList: MutableList<GoodsVolume> = mutableListOf()
    val goodsList: List<GoodsVolume>
        get() = _goodsList.toList()


    private val _viewState: MutableStateFlow<AppModel.ModelState> =
        MutableStateFlow(ModelState.Loading)
    val viewState: StateFlow<ModelState>
        get() = _viewState.asStateFlow()


    init {
        injectUseCases(this)
    }


    inline fun <reified T : GoodsVolume> onGetAllGoods(fileName: String) {
        val buffer: MutableList<GoodsVolume> = mutableListOf()
        scope.launch {
            getAllGoodsUseCase.invoke(fileName).collect {
                when (T::class) {
                    LocalBaseGoodsVolume::class -> {
                        try {
                            it[ROOT]
                            buffer.add(LocalBaseGoodsVolume(it))

                        } catch (e: Exception) {
                            onError("error with reading Local rating file")
                        }
                    }

                    CentralBaseGoodsVolume::class -> {
                        buffer.add(CentralBaseGoodsVolume(it))
                    }

                }
                onFillListDirectly(buffer)
            }

        }

    }


    fun onFillListDirectly(parsed: List<GoodsVolume>) {

        val goodsVolume: GoodsVolume?
        try {
            goodsVolume = parsed[ROOT]
        } catch (e: Exception) {
            onError("error with GoodsVolume Lists#1"); return
        }

        when (goodsVolume) {
            is LocalBaseGoodsVolume -> {
                try {
                    goodsVolume.list[INVOKE_EXCEPTION]
                    _goodsList.addAll(parsed)
                    onSuccessLocal();
                } catch (e: Exception) {
                    onError("error with GoodsVolume Lists#2"); return
                }
            }

            is CentralBaseGoodsVolume -> {
                _goodsList.addAll(parsed)
                onSuccessCentral()
            }

            else -> {
                onError("error with GoodsVolume Lists#3"); return
            }
        }
    }

    fun checkInvoiceStatus() {
        if (_goodsList.size == ENOUGH) {
            _viewState.value = ModelState.SuccessBothRatings;
            return
        }
    }

    fun onWriteInvoice(fileName: String) {
        scope.launch {
            writeAllGoods.invoke(fileName, goodsList)
        }
    }

    fun onError(string: String) {
        _viewState.value = ModelState.Error(string)
    }

    private fun onSuccessLocal() {
        _viewState.value = ModelState.SuccessLocal
    }

    private fun onSuccessCentral() {
        _viewState.value = ModelState.SuccessCentral
    }


    sealed class ModelState {
        object Loading : ModelState()
        object SuccessLocal : ModelState()
        object SuccessCentral : ModelState()
        object SuccessBothRatings : ModelState()
        data class Error(val errorCase: String) : ModelState()
    }


    companion object {
        const val ROOT = 0
        const val INVOKE_EXCEPTION = 1
        const val ENOUGH = 2
        fun buildModel(): AppModel {
            return AppModel()
        }
    }

}