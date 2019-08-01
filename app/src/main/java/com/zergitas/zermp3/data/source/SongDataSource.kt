package com.zergitas.zermp3.data.source

import com.zergitas.zermp3.data.model.Song

interface SongDataSource {
    interface Callback<T> {
        fun onGetDataSuccess(data: List<T>)

        fun onUpdateData(data:Pair<Int,T>)

        fun onFail(message: String)
    }

    /**
     * local data for song
     */
    interface LocalDataSource {
        fun getSongs(callback: Callback<Song>)

    }
}