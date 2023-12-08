package data.util.implementations


object InvoiceParser {
    object PropertiesSupplier {
        const val parseSchemaSize = 99
        const val resultFileSuffix = ".mxl"
        const val resultFilePrefix = "_inv_"
        const val cutTimeMillis = 7
        const val parseCharset = "Windows-1251"

        object Good {
            const val startAnchor = "Ц"
            const val secondAnchor = "Б"
            const val mockGood = "ЦБ999999"
            const val mockArg = 0
            const val anchorLength = 8
        }

        const val iterate = 1
    }
}
