package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class NavController(val startDestination: String) {

    private var navGraph: NavGraph? = null

    private var _navigationDestination by mutableStateOf<@Composable () -> Unit>({})
    val navigationDestination by derivedStateOf { _navigationDestination }
    var currentDestination by mutableStateOf("")
        private set

    init {
        NavGraph.init(this) {
            if (navGraph == null) navGraph = it
        }
    }

    fun navigate(route: String) {
        currentDestination = route
        navGraph?.onRequestDestination(route)
    }

    fun onNavigate(destination: @Composable () -> Unit, route: String) {
        if (route == currentDestination) {
            _navigationDestination = destination
        }
    }

}