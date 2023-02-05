package ui

import domain.useCases.DoWriteAllGoodUseCase
import domain.useCases.GetAllGoodsUseCase

interface InvoicerUIRepository {
    var writeAllGoods: DoWriteAllGoodUseCase
    var getAllGoodsUseCase: GetAllGoodsUseCase
}