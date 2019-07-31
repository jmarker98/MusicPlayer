package com.zergitas.zermp3.data.repository

import android.content.Context
import com.zergitas.zermp3.data.source.SettingDataSource

class SettingRepository(context: Context) : SettingDataSource.SettingLocalDataSource {
    private var cache: com.zergitas.zermp3.data.source.setting.Cache

    init {
        cache = com.zergitas.zermp3.data.source.setting.Cache(context)
    }

    override fun getLoop(callback: SettingDataSource.Callback<Boolean>) {
        callback.onGetDataSuccess(cache.getBoolean(com.zergitas.zermp3.data.source.setting.Cache.LOOP_SONG.first))
    }

    override fun onUpdateLoop(callback: SettingDataSource.Callback<Boolean>) {
        val isLoop = cache.getBoolean(com.zergitas.zermp3.data.source.setting.Cache.LOOP_SONG.first)
        cache.putBoolean(com.zergitas.zermp3.data.source.setting.Cache.LOOP_SONG.first, !isLoop)
        callback.onGetDataSuccess(!isLoop)
    }

}