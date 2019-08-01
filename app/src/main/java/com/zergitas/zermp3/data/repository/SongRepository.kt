package com.zergitas.zermp3.data.repository

import android.content.Context
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.source.SongDataSource
import com.zergitas.zermp3.data.source.local.song.SongLocalDataSource

class SongRepository(private val context: Context) : SongDataSource.LocalDataSource {

    //get data from local
    private var local: SongLocalDataSource

    //get data from remote

    init {
        local = SongLocalDataSource(context)
    }

    override fun getSongs(callback: SongDataSource.Callback<Song>) {
        local.getSongs(callback)
    }
}