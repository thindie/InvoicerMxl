package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    graphs: NavGraph.Companion.() -> Unit
) {
    graphs.invoke(NavGraph.Companion)
    Box(modifier) {
        navController.navigationDestination.invoke()
    }
}
