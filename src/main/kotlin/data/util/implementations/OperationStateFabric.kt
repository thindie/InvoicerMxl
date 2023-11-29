package data.util.implementations

import domain.OperationState

class OperationStateFabric private constructor(){
    companion object{
        fun loading(): OperationState{
           return FileIOOperationsState(isSuccess = false, isLoading = true, isError = false, standBy = false)
        }
        fun error(): OperationState{
            return FileIOOperationsState(isSuccess = false, isLoading = false, isError = true, standBy = false)
        }
        fun success(): OperationState{
            return FileIOOperationsState(isSuccess = true, isLoading = false, isError = false, standBy = false)
        }
        fun awaits(): OperationState{
            return FileIOOperationsState(isSuccess = false, isLoading = false, isError = false, standBy = true)
        }
    }
    private data class FileIOOperationsState(
        override val isSuccess: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val standBy: Boolean
    ) : OperationState
}