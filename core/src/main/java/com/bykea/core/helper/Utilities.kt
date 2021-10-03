package com.bykea.core.helper

import com.bykea.core.model.NetworkResult
import com.squareup.moshi.Moshi
import java.lang.reflect.ParameterizedType


object Utilities {
    fun <T> mapModel(t: T, response: String, type: ParameterizedType): NetworkResult<T> {
        return try {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter<T>(type)
            val fromJson = adapter.fromJson(response)
            if (fromJson != null) {
                NetworkResult.Success(fromJson)
            } else {
                NetworkResult.Error(java.lang.Exception("Something went wrong"))
            }

        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

    }
}