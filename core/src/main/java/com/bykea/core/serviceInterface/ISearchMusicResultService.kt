package com.bykea.core.serviceInterface

import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel

interface ISearchMusicResultService {
    suspend fun getSearchMusicResults(query: String): NetworkResult<SearchModel>
}