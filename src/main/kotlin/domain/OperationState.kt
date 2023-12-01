package domain

import domain.entities.Engine

interface OperationState {
     val currentState: Engine
}