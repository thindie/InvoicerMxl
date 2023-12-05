package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class NavGraph private constructor(private val navController: NavController) {

    private var shouldShowStartDestination by mutableStateOf(true)

    private val coordinations = mutableMapOf<String, @Composable () -> Unit>()

    fun onRequestDestination(route: String) {
        val destinationAction = coordinations[route]
        navController.onNavigate(destination = destinationAction ?: {}, route = route)
    }


    companion object {
        private var navGraph: NavGraph? = null
        private var startDestination: String? = null
        private var navController: NavController? = null
        fun route(route: String, destinationAction: @Composable () -> Unit) {
            navGraph?.coordinations?.put(route, destinationAction)
            if (navGraph?.shouldShowStartDestination == true && route == startDestination) {
                navGraph?.shouldShowStartDestination = false
                navController?.navigate(startDestination ?: "")
            }
        }

        fun init(navController: NavController, initNavGraph: (NavGraph) -> Unit) {
            if (this.navController == null) {
                this.navController = navController
                navGraph = NavGraph(this.navController!!)
                startDestination = this.navController!!.startDestination
                initNavGraph(requireNotNull(navGraph))
            }
        }
    }
}

