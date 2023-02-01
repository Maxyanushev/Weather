package com.example.myweatherapp.data

import retrofit2.Response
import java.net.*

suspend fun <T> call(call: suspend () -> Response<T>): Resource<T?> {
    runCatching {
        val response = call.invoke()
        return if (response.isSuccessful) {
            try {
                Resource.Success(response.body())
            } catch (e: Exception) {
                Resource.Error(throwable = e)
            }
        } else {
            if (response.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Resource.Success(null)
            } else {
                parseServerError(response)
            }
        }
    }.onFailure {
        return Resource.Error(
            throwable = it
        )
    }
    return Resource.Error(throwable = UnsupportedOperationException())
}

private fun <T> parseServerError(response: Response<T>): Resource<T?> {
    var exception = Throwable("Error occurred when calling api")
    try {
        response.errorBody()?.string()?.let { responseJson ->
            exception = Throwable(responseJson)
        }
    } catch (e: Exception) {
        // ignore
    }
    return Resource.Error(null, exception)
}