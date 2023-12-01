import ui.EntryPoint
import javax.swing.UIManager

const val TITLE = "INVOICER MXL for 1c v.7.7"

fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    EntryPoint.launchApplication()
}











