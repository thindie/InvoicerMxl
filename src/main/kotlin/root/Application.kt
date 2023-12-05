package root


abstract class Application {
    abstract val viewModelStoreOwner: ViewModelStoreOwner

    private fun setViewModelStoreOwner() {
        if (storeOwner == null) {
            storeOwner = this.viewModelStoreOwner
        }
    }

    init {
        setViewModelStoreOwner()
    }

    companion object {
        var storeOwner: ViewModelStoreOwner? = null
            private set

        inline fun <reified T : ViewModel> viewModel(): T {
            return requireNotNull(storeOwner)
                .getViewModel(T::class.java.kotlin) as T
        }
    }
}