package com.bykea.network

import com.bykea.core.helper.Constants.errorMessage
import com.bykea.core.helper.HttpRequestHelper
import com.bykea.core.helper.Utilities
import com.bykea.core.model.NetworkResult
import com.bykea.core.networkInterface.IBaseNetwork
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.reflect.ParameterizedType

open class BaseNetworkImpl : IBaseNetwork, KoinComponent {

    private val httpRequestHelper: HttpRequestHelper by inject()

    override suspend fun <T> get(
        baseUrl: String,
        endPoint: String,
        params: HashMap<String, String>,
        type: T,
        parameterizedType: ParameterizedType
    ): NetworkResult<T> {

        val url = baseUrl + endPoint

        when (val httpResponse = httpRequestHelper.get(url = url, params = params)) {
            is NetworkResult.Success -> {
                httpResponse.data.apply {
                    when (code()) {
                        200 -> {
                            val response = body()?.string()
                            response?.let {
                                return when (val mapObject =
                                    Utilities.mapModel(type, it, parameterizedType)) {
                                    is NetworkResult.Success -> {
                                        NetworkResult.Success(mapObject.data)
                                    }
                                    is NetworkResult.Error -> {
                                        NetworkResult.Error(mapObject.exception)
                                    }
                                }
                            }
                            return NetworkResult.Error(Exception(errorMessage))
                        }
                        else -> {
                            return NetworkResult.Error(Exception(errorMessage))
                        }
                    }
                }
            }
            is NetworkResult.Error -> {
                return NetworkResult.Error(httpResponse.exception)
            }
        }
    }
}