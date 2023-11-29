import ui.EntryPoint
import javax.swing.UIManager

const val OPEN_LOCAL_RATING = "Open Local Rating File"
const val OPEN_CENTRAl_RATING = "Open Central Rating File"
const val OPEN_ANOTHER_RATING = "Open Another Rating"
const val MOCK_CENTRAL_RATING = "Mock Local Rating File"
const val PARSE = "Parsed"
const val SAVE_FILE = "Save file"
const val MOCK = "Mocked"
const val TITLE = "INVOICER MXL for 1c v.7.7"
const val CUT = ""
const val AS = " as "
const val BASE = "Base"


fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    EntryPoint.launchApplication()
}











