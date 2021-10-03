package com.bykea.casestudy.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel
import com.bykea.core.serviceInterface.ISearchMusicResultService
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicViewModel : ViewModel(), KoinComponent {

    private val searchMusicResultService: ISearchMusicResultService by inject()
    val searchModel: MutableLiveData<NetworkResult<SearchModel>> = MutableLiveData()

    fun getSearchResults(query: String) = viewModelScope.launch {
        when (val response = searchMusicResultService.getSearchMusicResults(query = query)) {
            is NetworkResult.Success -> searchModel.postValue(response)
            is NetworkResult.Error -> searchModel.postValue(NetworkResult.Error(response.exception))
        }
    }
}