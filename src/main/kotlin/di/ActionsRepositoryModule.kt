package di

import dagger.Binds
import dagger.Module
import data.InvoiceOperatorImpl
import domain.ActionsRepository

@Module
interface ActionsRepositoryModule {
    @Binds
    fun bindActionsRepository(impl: InvoiceOperatorImpl): ActionsRepository
}