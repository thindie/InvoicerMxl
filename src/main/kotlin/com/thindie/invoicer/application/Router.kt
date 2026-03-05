package com.thindie.invoicer.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.Serializable
import java.util.*

class Router {
    private val current = MutableStateFlow<List<Route>?>(null)
    val currentFlow: Flow<Route?> = current.map { it?.lastOrNull() }
    val poppedOut = MutableSharedFlow<Unit>()

    fun push(route: Route) {
        current.update { if (it.isNullOrEmpty()) listOf(route) else it + route }
    }

    fun pop() {
        current.update {
            if (it.isNullOrEmpty()) {
                poppedOut.tryEmit(Unit)
                null
            } else {
                val route = current.value?.lastOrNull()
                route?.dispose()
                var newStack: List<Route>? = it.dropLast(1)
                if (newStack.isNullOrEmpty()) {
                    poppedOut.tryEmit(Unit)
                    newStack = null
                }
                newStack
            }
        }
    }
}

interface Route {
    val id: String
    val content: @Composable () -> Unit
    fun dispose()
}

object RouteFactory {
    fun <C : Command, S : State> create(
        state: () -> S,
        wire: (C) -> S,
        content: @Composable (S, MutableSharedFlow<C>) -> Unit
    ): Route {
        return object : Route {
            private val commands = MutableSharedFlow<C>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

            override val id: String
                get() = UUID.randomUUID().toString()

            override val content: @Composable () -> Unit
                get() = {
                    val saveableState = rememberSaveable { mutableStateOf<S?>(state()) }

                    LaunchedEffect(Unit) {
                        commands
                            .filterNot { it is Dispose }
                            .collect {
                                saveableState.value = wire(it)
                            }

                        commands
                            .filter { it is Dispose }
                            .collect {
                                saveableState.value = null
                            }

                    }

                    if (saveableState.value != null) {
                        content(requireNotNull(saveableState.value), commands)
                    }
                }

            override fun dispose() {
                commands.send(Dispose as C)
            }
        }
    }
}

fun <C : Command> MutableSharedFlow<C>.send(command: C) = tryEmit(command)

interface Command

private data object Dispose : Command

interface State : Serializable