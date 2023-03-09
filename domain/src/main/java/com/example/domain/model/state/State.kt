package com.example.domain.model.state


sealed class State<T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<T> : State<T>()
    class Success<T>(data: T) : State<T>(data)
    class Error<T>(error: Throwable) : State<T>(error = error)
}
