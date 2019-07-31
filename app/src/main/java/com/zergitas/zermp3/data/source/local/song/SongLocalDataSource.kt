package com.zergitas.zermp3.data.source.local.song

import android.content.Context
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.source.SongDataSource

class SongLocalDataSource (private val context: Context): SongDataSource.LocalDataSource {
    override fun getSongs(callback: SongDataSource.Callback<Song>) {
        GetSongsAsyncTask(callback).execute(context)
    }
}