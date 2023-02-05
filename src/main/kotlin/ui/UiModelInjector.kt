package ui


import data.GoodParserRepositoryImpl
import domain.useCases.DoWriteAllGoodUseCase
import domain.useCases.GetAllGoodsUseCase

fun injectUseCases(impl: InvoicerUIRepository) {
    val goodParserRepository = GoodParserRepositoryImpl
    impl.writeAllGoods = DoWriteAllGoodUseCase(goodParserRepository)
    impl.getAllGoodsUseCase = GetAllGoodsUseCase(goodParserRepository)
}



