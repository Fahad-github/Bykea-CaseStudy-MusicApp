package com.bykea.network

import com.bykea.core.helper.Constants
import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel
import com.bykea.core.networkInterface.ISearchMusicResultNetwork
import com.squareup.moshi.Types
import retrofit2.http.Query

class SearchMusicResultNetworkImpl : BaseNetworkImpl(), ISearchMusicResultNetwork {

    override suspend fun getSearchMusicResults(query: String): NetworkResult<SearchModel> {
        val parameterizedType =
            Types.newParameterizedType(SearchModel::class.java, SearchModel::class.java)

        val queryMap = hashMapOf(
            "entity" to "musicTrack",
            "term" to query,
            "limit" to "30"
        )

        val response = get(
            baseUrl = Constants.baseUrl,
            endPoint = Constants.endPont,
            params = queryMap,
            type = SearchModel(),
            parameterizedType = parameterizedType
        )

        return when (response) {
            is NetworkResult.Success -> {
                NetworkResult.Success(response.data)
            }
            is NetworkResult.Error -> {
                NetworkResult.Error(response.exception)
            }
        }
    }

}