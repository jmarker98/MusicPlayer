package com.zergitas.zermp3.screen.playsong

import android.content.Context
import com.zergitas.zermp3.data.repository.SettingRepository
import com.zergitas.zermp3.data.source.SettingDataSource

class PlaySongPresenter(context: Context): PlaySongContract.Presenter {
    private var view: PlaySongContract.View? = null
    private var settingRepository: SettingRepository

    init {
        settingRepository = SettingRepository(context)
    }

    override fun onUpdateLoopSong() {
        settingRepository.onUpdateLoop(object : SettingDataSource.Callback<Boolean> {
            override fun onGetDataSuccess(data: Boolean) {
                view?.showLoopSetting(data)
            }
        })
    }

    override fun getLoopSong() {
        settingRepository.getLoop(object : SettingDataSource.Callback<Boolean> {
            override fun onGetDataSuccess(data: Boolean) {
                view?.showLoopSetting(data)
            }
        })
    }

    override fun setView(view: PlaySongContract.View) {
        this.view = view
    }

    override fun start() {
        getLoopSong()
    }

    override fun stop() {
    }
}