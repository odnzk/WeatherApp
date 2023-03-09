package com.example.data.repository

import com.example.domain.model.state.State
import com.example.domain.util.ConnectionLostException
import com.example.domain.util.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

open class BaseNetworkRepository {
    suspend inline fun <T> safeRequest(crossinline request: suspend () -> T): State<T> =
        withContext(Dispatchers.IO) {
            try {
                val result = request()
                State.Success(result)
            } catch (e: IOException) {
                State.Error(ConnectionLostException())
            } catch (e: HttpException) {
                State.Error(NetworkException(e.code(), e.message()))
            }
        }
}
