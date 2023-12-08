package di

import dagger.Binds
import dagger.Module
import data.InvoiceOperatorImpl
import data.ShareOperatorImpl
import domain.ActionsRepository
import domain.ShareRepository

@Module
interface RepositoriesModule {
    @Binds
    fun bindActionsRepository(impl: InvoiceOperatorImpl): ActionsRepository

    @Binds
    fun bindShareRepository(impl: ShareOperatorImpl): ShareRepository
}