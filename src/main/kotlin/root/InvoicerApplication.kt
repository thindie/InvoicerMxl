package root

import di.AppComponent
import ui.feature_invoice.viewmodel.InvoiceScreenViewModel
import ui.feature_share.viewmodel.ShareScreenViewModel
import javax.inject.Inject
import kotlin.reflect.KClass


open class InvoicerApplication @Inject constructor(
) : Application(), ViewModelStoreOwner {

   protected var daggerAppComponent: AppComponent? = null
    override val viewModelStoreOwner: ViewModelStoreOwner
        get() = this

    private val viewModelStore = mutableMapOf<KClass<out ViewModel>, ViewModel>()

    @Inject
    lateinit var invoiceScreenViewModel: InvoiceScreenViewModel

    @Inject
    lateinit var screenViewModel: ShareScreenViewModel

    init {
        onCreate()
    }




    override fun getViewModel(viewModelClass: KClass<out ViewModel>): ViewModel {
        return requireNotNull(viewModelStore[viewModelClass])
    }

    override fun setViewModel(viewModelClass: KClass<out ViewModel>, viewModel: ViewModel) {
        viewModelStore.put(viewModelClass, viewModel)
    }

    private fun onCreate() {
        daggerAppComponent = initDaggerComponent()
        daggerAppComponent?.inject(this)
        setViewModel(invoiceScreenViewModel::class.java.kotlin, invoiceScreenViewModel)
        setViewModel(screenViewModel::class.java.kotlin, screenViewModel)
    }

    private fun initDaggerComponent(): AppComponent {
        return if (this.daggerAppComponent == null) {
            AppComponent.init()
        } else checkNotNull(daggerAppComponent)
    }

}
