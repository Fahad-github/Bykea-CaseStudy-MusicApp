package com.bykea.core.networkInterface

import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel

interface ISearchMusicResultNetwork {
    suspend fun getSearchMusicResults(query: String): NetworkResult<SearchModel>
}