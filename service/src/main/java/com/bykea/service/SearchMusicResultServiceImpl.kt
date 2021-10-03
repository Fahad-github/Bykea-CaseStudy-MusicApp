package com.bykea.service

import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel
import com.bykea.core.networkInterface.ISearchMusicResultNetwork
import com.bykea.core.serviceInterface.ISearchMusicResultService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

class SearchMusicResultServiceImpl : ISearchMusicResultService, KoinComponent {

    private val searchMusicResultNetwork: ISearchMusicResultNetwork by inject()

    override suspend fun getSearchMusicResults(query: String): NetworkResult<SearchModel> {
        return when (val activity = searchMusicResultNetwork.getSearchMusicResults(query)) {
            is NetworkResult.Success -> {
                NetworkResult.Success(activity.data)
            }
            is NetworkResult.Error -> {
                NetworkResult.Error(Exception("Something went wrong"))
            }
        }
    }


}