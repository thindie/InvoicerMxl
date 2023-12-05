package root

import kotlin.reflect.KClass

interface ViewModelStoreOwner {
    fun getViewModel(viewModelClass: KClass<out ViewModel>): ViewModel
    fun setViewModel(viewModelClass: KClass<out ViewModel>, viewModel: ViewModel)
}