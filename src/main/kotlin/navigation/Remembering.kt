package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberNavController(startDestination: String): NavController {
    return remember { NavController(startDestination = startDestination) }
}