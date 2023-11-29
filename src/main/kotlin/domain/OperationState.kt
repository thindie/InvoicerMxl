package domain

interface OperationState {
     val isSuccess: Boolean
     val isLoading: Boolean
     val isError: Boolean
     val standBy: Boolean
}