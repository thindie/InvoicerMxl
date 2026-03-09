package com.thindie.invoicer.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.coroutines.cancellation.CancellationException

@Stable
class Router(val onPopLast: () -> Unit) {
  private val current = MutableStateFlow<List<Route>>(emptyList())


  val route = current
	.map { routes ->
	  val lastRoute = routes.lastOrNull()
	  val veryLastRoute = if (lastRoute != null) {
		val reducedRoutes = routes.dropLast(1)
		reducedRoutes.lastOrNull()
	  } else null
	  if (lastRoute == null) return@map null
	 lastRoute to veryLastRoute
	}
	.filterNotNull()

  @Stable
  fun push(route: Route) {
	current.update { it + route }
  }

  @Stable
  fun pop() {
	current.update { routes ->
	  val route = routes.lastOrNull()
	  if (route != null) {
		val newStack = routes - route
		route.dispose()
		if (newStack.isEmpty()) {
		  onPopLast()
		}
		newStack
	  } else {
		onPopLast()
		emptyList()
	  }
	}
  }

  @Stable
  fun removeAll(ids: Set<Route.Id>) {
	current.update { routes ->
	  routes.mapNotNull { route ->
		if (route.id in ids) {
		  route.dispose()
		  null
		} else {
		  route
		}
	  }
	}
  }
}

@Stable
interface Route {
  val id: Id
  val content: @Composable () -> Unit

  @Stable
  fun dispose()

  @JvmInline
  value class Id(val id: String)
}

@Stable
object RouteFactory {

  @Stable
  fun <C : Command, S : State> create(
	initialState: S,
	execute: suspend (c: C, s: S) -> S,
	errorMapper: (e: Throwable) -> ScreenScopeError = { _ ->
	  ScreenScopeError(
		message = "somenthing wrong",
		actions = mapOf(),
	  )
	},
	routeContent: @Composable ScreenScope<S, C>.() -> Unit
  ): Route {
	return object : Route {

	  @Stable
	  private val disposeCommand = MutableSharedFlow<C>(
		replay = 0,
		extraBufferCapacity = 1,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	  )

	  @Stable
	  var screenScope: ScreenScope<S, C>? = object : ScreenScope<S, C> {

		private var scope: CoroutineScope? =
		  CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { _, _ -> })

		private val _state = mutableStateOf(initialState)
		override val state: androidx.compose.runtime.State<S>
		  get() = _state

		private val _processing = mutableStateOf<C?>(null)
		override val processing: androidx.compose.runtime.State<C?>
		  get() = _processing

		private val _error = mutableStateOf<ScreenScopeError?>(null)
		override val error: androidx.compose.runtime.State<ScreenScopeError?>
		  get() = _error

		override fun send(command: C) {
		  scope?.launch {
			when (command) {
			  ServiceCommand.Dispose -> {
				dispose()
				disposeCommand.tryEmit(command)
			  }

			  else -> {
				if (_error.value == null) {
				  try {
					_processing.value = command
					val newState = execute(command, _state.value)
					_state.value = newState
					_processing.value = null
				  } catch (e: CancellationException) {
					dispose()
					disposeCommand.tryEmit(command)
					throw e
				  } catch (e: Throwable) {
					val error = errorMapper(e)
					_error.value = error
					_processing.value = null
				  }
				} else {
				  try {
					_processing.value = command
					val newState = execute(command, _state.value)
					_state.value = newState
					_error.value = null
					_processing.value = null
				  } catch (e: CancellationException) {
					dispose()
					disposeCommand.tryEmit(command)
					throw e
				  } catch (e: Throwable) {
					val error = errorMapper(e)
					_error.value = error
					_processing.value = null
				  }
				}
			  }
			}
		  }
		}

		override fun dispose() {
		  scope?.cancel()
		  scope = null
		}
	  }

	  @Stable
	  override val id: Route.Id = Route.Id(UUID.randomUUID().toString())


	  override val content: @Composable () -> Unit = {
		LaunchedEffect(initialState, id) {
		  disposeCommand.collect { _ -> screenScope = null }
		}
		screenScope?.routeContent()
	  }

	  @Stable
	  override fun dispose() {
		disposeCommand.send(ServiceCommand.Dispose as C)
	  }
	}
  }
}

fun <C : Command> MutableSharedFlow<C>.send(command: C) = tryEmit(command)
