package com.bykea.casestudy.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bykea.casestudy.R
import com.bykea.casestudy.base.BaseActivity
import com.bykea.casestudy.common.changingTextListener
import com.bykea.casestudy.common.showToast
import com.bykea.casestudy.databinding.ActivityMainBinding
import com.bykea.casestudy.listener.MusicItemClickListener
import com.bykea.core.helper.Constants
import com.bykea.core.model.NetworkResult
import com.bykea.core.model.SearchModel
import com.bykea.core.model.SearchResultModel

class MainActivity : BaseActivity(), MusicItemClickListener {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    private val viewModel: MusicViewModel by viewModels()
    private val musicAdapter = MusicAdapter()
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private var handler = Handler()
    private lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BykeaCaseStudy)
        musicAdapter.musicItemClickListener = this
        subscribeObservers()
        initialiseListeners()
    }

    private fun initialiseListeners() {
        binding.editTextSearch.changingTextListener { it ->
            if (it.length > 3) {
                Looper.myLooper()?.let { looper ->
                    Handler(looper).postDelayed({
                        displayProgressBar(isDisplayed = true)
                        viewModel.getSearchResults(it)
                    }, 300)
                }

            }
        }

        binding.buttonPlayPause.setOnClickListener {
            if (!mediaPlayer?.isPlaying!!) {
                mediaPlayer?.start()
                binding.buttonPlayPause.setImageResource(R.drawable.ic_pause)
            } else {
                mediaPlayer?.pause()
                binding.buttonPlayPause.setImageResource(R.drawable.ic_play)
            }
        }

        binding.seekBar.progress = 0

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) mediaPlayer?.seekTo(pos)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })


        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer?.currentPosition ?: 0
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)
        mediaPlayer?.setOnCompletionListener {
            binding.buttonPlayPause.setBackgroundResource(R.drawable.ic_play)
            binding.seekBar.progress = 0
        }
    }

    private fun subscribeObservers() {
        viewModel.searchModel.observe(this, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    displayProgressBar(isDisplayed = false)
                    recyclerViewHelper(false)
                    showToast(response.exception.message ?: Constants.errorMessage)
                }
                is NetworkResult.Success -> {
                    displayProgressBar(isDisplayed = false)
                    response.data.let {
                        recyclerViewHelper(
                            hasData = it.resultCount > 0,
                            data = it
                        )
                    }
                }
            }
        })
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun recyclerViewHelper(hasData: Boolean, data: SearchModel? = null) {
        if (hasData) {
            binding.textViewNoResultsFound.visibility = View.GONE
            musicAdapter.setMusicSearchResultList(data?.results as ArrayList<SearchResultModel>)
            binding.recyclerViewSongs.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = musicAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        this@MainActivity,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        } else {
            musicAdapter.clearRecycler()
            binding.textViewNoResultsFound.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(item: SearchResultModel) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.let {
            it.setDataSource(item.previewUrl)
            it.prepareAsync()
            it.setOnPreparedListener { mp ->
                mp.start()
                binding.rlPlayer.visibility = View.VISIBLE
                binding.seekBar.max = mediaPlayer?.duration ?: 0
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}