package com.bykea.casestudy.listener

import com.bykea.core.model.SearchResultModel

interface MusicItemClickListener {
    fun onItemClicked(item:SearchResultModel)
}