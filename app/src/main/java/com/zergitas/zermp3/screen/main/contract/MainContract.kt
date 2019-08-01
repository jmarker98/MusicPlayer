package com.zergitas.zermp3.screen.main.contract

import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.BasePresenter

interface MainContract {
    /**
     * view
     */
    interface View {
        fun showProgressBar()

        fun hiddenProgressBar()

        fun addSongs(songs: List<Song>)

        fun updateSongs(pair:Pair<Int,Song>)

        fun showErrorSongs()

    }

    /**
     * presenter
     */
    interface Presenter : BasePresenter<View> {
        fun getSongs()
    }

}