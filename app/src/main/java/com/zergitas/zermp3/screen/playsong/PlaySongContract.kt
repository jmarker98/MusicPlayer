package com.zergitas.zermp3.screen.playsong

import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.BasePresenter

class PlaySongContract {
    /**
     * view
     */
    interface View {

        fun getSongs(): ArrayList<Song>

        fun getIndexCurrent(): Int

        fun isOpenFromNoti(): Boolean

        fun showLoopSetting(isLoop: Boolean)
    }

    /**
     * presenter
     */
    interface Presenter : BasePresenter<View> {

        fun onUpdateLoopSong()

        fun getLoopSong()
    }
}