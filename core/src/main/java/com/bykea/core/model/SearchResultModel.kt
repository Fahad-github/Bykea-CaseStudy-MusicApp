package com.bykea.core.model

data class SearchResultModel(
    var artistId: Int,
    var artistName: String,
    var artistViewUrl: String,
    var artworkUrl100: String,
    var artworkUrl30: String,
    var artworkUrl60: String,
    var collectionCensoredName: String,
    var collectionExplicitness: String,
    var collectionId: Int,
    var collectionName: String,
    var collectionPrice: Double,
    var collectionViewUrl: String,
    var country: String,
    var currency: String,
    var discCount: Int,
    var discNumber: Int,
    var isStreamable: Boolean,
    var kind: String,
    var previewUrl: String,
    var primaryGenreName: String,
    var releaseDate: String,
    var trackCensoredName: String,
    var trackCount: Int,
    var trackExplicitness: String,
    var trackId: Int,
    var trackName: String,
    var trackNumber: Int,
    var trackPrice: Double,
    var trackTimeMillis: Int,
    var trackViewUrl: String,
    var wrapperType: String,
    var inProgress: Boolean = false
)