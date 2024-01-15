package di

import dagger.Binds
import dagger.Module
import data.CopyInvoiceRepositoryImpl
import data.InvoiceOperatorImpl
import data.ShareOperatorImpl
import domain.ActionsRepository
import domain.CopyInvoiceRepository
import domain.ShareRepository

@Module
interface RepositoriesModule {
    @Binds
    fun bindActionsRepository(impl: InvoiceOperatorImpl): ActionsRepository

    @Binds
    fun bindShareRepository(impl: ShareOperatorImpl): ShareRepository

    @Binds
    fun bindCopyInvoice(impl: CopyInvoiceRepositoryImpl): CopyInvoiceRepository
}