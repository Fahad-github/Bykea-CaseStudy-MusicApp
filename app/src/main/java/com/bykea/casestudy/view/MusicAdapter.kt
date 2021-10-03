package com.bykea.casestudy.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bykea.casestudy.R
import com.bykea.casestudy.common.setImageUrl
import com.bykea.casestudy.databinding.MusicSearchResultItemBinding
import com.bykea.casestudy.listener.MusicItemClickListener
import com.bykea.core.model.SearchResultModel

@SuppressLint("NotifyDataSetChanged")
class MusicAdapter : RecyclerView.Adapter<MusicAdapter.MusicItemViewHolder>() {

    var musicSearchResultList: List<SearchResultModel> = ArrayList()
    var musicItemClickListener: MusicItemClickListener? = null


    inner class MusicItemViewHolder(
        private val parent: ViewGroup,
        private val binding: MusicSearchResultItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.music_search_result_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemView.setOnClickListener {
                musicItemClickListener?.onItemClicked(item = musicSearchResultList[adapterPosition])
                setIsPlayingFalse()
                musicSearchResultList[adapterPosition].inProgress = true
                notifyDataSetChanged()
            }
        }

        fun setData(musicItem: SearchResultModel) {
            binding.lottiePlayingSong.visibility = if(musicItem.inProgress) View.VISIBLE else View.GONE
            binding.textViewSong.text = musicItem.trackName
            binding.textViewArtist.text = musicItem.artistName
            binding.textViewAlbum.text = musicItem.collectionName
            binding.imageViewArtist.setImageUrl(musicItem.artworkUrl100)
        }

    }

    private fun setIsPlayingFalse() {
        musicSearchResultList.forEach { it.inProgress = false }
    }


    fun clearRecycler() {
        this.musicSearchResultList = ArrayList()
        notifyDataSetChanged()
    }

    fun setMusicSearchResultList(imageList: ArrayList<SearchResultModel>) {
        this.musicSearchResultList = imageList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemViewHolder {
        return MusicItemViewHolder(parent)
    }

    override fun onBindViewHolder(holderMusic: MusicItemViewHolder, position: Int) {
        holderMusic.setData(musicSearchResultList[position])
    }

    override fun getItemCount(): Int {
        return musicSearchResultList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
