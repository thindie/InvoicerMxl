package data.util.implementations

import domain.OperationState
import domain.entities.Engine

class OperationStateFabric private constructor(){
    companion object{
        fun loading(): OperationState{
           return FileIOOperationsState(Engine.LOAD)
        }
        fun error(): OperationState{
            return FileIOOperationsState(Engine.ERROR)
        }
        fun success(): OperationState{
            return FileIOOperationsState(Engine.SUCCESS)
        }
        fun awaits(): OperationState{
            return FileIOOperationsState(Engine.STANDBY)
        }
    }
    private data class FileIOOperationsState(
         val engine: Engine
    ) : OperationState {
        override val currentState = engine
    }
}