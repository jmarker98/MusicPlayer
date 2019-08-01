package com.zergitas.zermp3.screen.main.presenter

import android.content.Context
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.repository.SongRepository
import com.zergitas.zermp3.data.source.SongDataSource
import com.zergitas.zermp3.screen.main.contract.MainContract

class MainPresenter(context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = null
    private var songRepository: SongRepository

    init {
        songRepository = SongRepository(context)
    }

    override fun getSongs() {
        songRepository.getSongs(object : SongDataSource.Callback<Song> {
            override fun onUpdateData(data: Pair<Int, Song>) {
                view?.updateSongs(data)
            }

            override fun onGetDataSuccess(data: List<Song>) {
                view?.addSongs(data)
            }

            override fun onFail(message: String) {
                view?.showErrorSongs()
            }

        })
    }


    override fun setView(view: MainContract.View) {
        this.view = view
    }

    override fun start() {
    }

    override fun stop() {
    }
}