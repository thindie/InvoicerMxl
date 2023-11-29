package domain

import domain.entities.PathType
import kotlinx.coroutines.flow.Flow

interface ActionsRepository {
    fun observeActionsResult(): Flow<Event<OperationState>>
    fun applyPath(pathProvider: PathProvider, pathType: PathType)
    fun requestAction()
}