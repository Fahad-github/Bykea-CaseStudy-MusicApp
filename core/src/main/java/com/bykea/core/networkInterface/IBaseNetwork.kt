package com.bykea.core.networkInterface

import com.bykea.core.model.NetworkResult
import java.lang.reflect.ParameterizedType

interface IBaseNetwork {


    suspend fun <T> get(
        baseUrl: String,
        endPoint: String,
        params: HashMap<String, String>,
        type: T,
        parameterizedType: ParameterizedType
    ): NetworkResult<T>
}